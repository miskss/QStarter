package com.qstarter.admin.service;

import com.qstarter.admin.model.dto.ModifySysHtmlDTO;
import com.qstarter.admin.model.dto.SysHtmlDTO;
import com.qstarter.admin.model.vo.HtmlTree;
import com.qstarter.admin.model.vo.SysHtmlDetailVO;
import com.qstarter.admin.model.vo.ZTreePageVO;
import com.qstarter.core.constant.CacheName;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.utils.EnumUtils;
import com.qstarter.security.entity.SystemHtml;
import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.enums.HtmlType;
import com.qstarter.security.repository.SystemHtmlRepository;
import com.qstarter.security.service.SystemResourcesService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-11-07 11:00
 **/
@Service
@Transactional
@CacheConfig(cacheNames = CacheName.SYSTEM_ROLE)
public class SystemHtmlService {

    private static final String SYS_HTML_COLLECTION = "'syshtmlcollection'";
    private static final String SYS_ZTREE_COLLECTION = "'ztreecollection'";

    private SystemHtmlRepository repository;

    private SystemResourcesService resourcesService;

    public SystemHtmlService(SystemHtmlRepository systemHtmlRepository, SystemResourcesService resourcesService) {
        this.repository = systemHtmlRepository;
        this.resourcesService = resourcesService;
    }

    /**
     * ztree 树的数据结构
     *
     * @return {@link List<ZTreePageVO>}
     */
    @Cacheable(key = SYS_ZTREE_COLLECTION)
    public List<ZTreePageVO> findZTree() {
        List<SystemHtml> all = repository.findAll();
        return all.stream()
                .map(html -> {
                    Integer id = html.getId();
                    String name = html.getHtmlName();
                    SystemHtml parent = html.getParent();
                    return new ZTreePageVO(id, name, parent == null ? 0 : parent.getId(),html.getSortNum());
                }).sorted(Comparator.comparingInt(ZTreePageVO::getSortNum))

                .collect(Collectors.toList());

    }

    /**
     * 普通的树形结构数据
     *
     * @return {@link  List<HtmlTree>}
     */
    @Cacheable(key = SYS_HTML_COLLECTION)
    public List<HtmlTree> findHtmlTree() {
        List<HtmlTree> list = new ArrayList<>();
        //找出根节点下的所有的节点（即父节点为null）
        List<SystemHtml> byParentIsNull1 = repository.findByParentIsNull();
        if (byParentIsNull1.isEmpty()) return list;
        byParentIsNull1.sort(Comparator.comparingInt(SystemHtml::getSortNum));
        return byParentIsNull1.stream().map(this::findTree).collect(Collectors.toList());
    }

    public HtmlTree findTree(SystemHtml systemHtml) {
        List<SystemHtml> sub_child = repository.findByParent_Id(systemHtml.getId());
        if (sub_child == null || sub_child.isEmpty()) {
            return HtmlTree.fromEntity(systemHtml);
        }
        sub_child.sort(Comparator.comparingInt(SystemHtml::getSortNum));
        List<HtmlTree> collect = sub_child.stream().map(this::findTree).collect(Collectors.toList());
        HtmlTree htmlTree = HtmlTree.fromEntity(systemHtml);
        htmlTree.setChildren(collect);
        return htmlTree;
    }


    public Page<SystemHtml> list(int pageIndex, int pageSize) {
        return repository.findAll(PageRequest.of(pageIndex, pageSize, Sort.Direction.DESC, "createTime"));
    }

    @Caching(evict = {
            @CacheEvict(key = SYS_HTML_COLLECTION),
            @CacheEvict(key = SYS_ZTREE_COLLECTION),
    })
    public void add(SysHtmlDTO dto) {
        String htmlName = dto.getHtmlName();
        String htmlType = dto.getHtmlType();

        HtmlType type = EnumUtils.convertToEnum(HtmlType.class, htmlType);

        SystemHtml parent = null;
        Integer parentId = dto.getParentId();
        if (parentId != null) {
            parent = findOne(parentId);
        }
        List<SystemResource> resources = findResources(dto.getResourceIds());
        SystemHtml of = SystemHtml.of(htmlName, type, dto.getHtmlAddr(), dto.getSortNum(), dto.getIconUrl(), dto.getAlias(), parent, resources);
        repository.save(of);
    }

    @Caching(evict = {
            @CacheEvict(key = SYS_HTML_COLLECTION),
            @CacheEvict(key = SYS_ZTREE_COLLECTION),
    })
    public void update(ModifySysHtmlDTO dto) {
        SystemHtml one = findOne(dto.getHtmlId());
        one.setHtmlName(dto.getHtmlName());
        one.setHtmlType(EnumUtils.convertToEnum(HtmlType.class, dto.getHtmlType()));
        SystemHtml parent = null;
        Integer parentId = dto.getParentId();
        if (parentId != null) {
            parent = findOne(parentId);
        }
        one.setHtmlAddr(dto.getHtmlAddr());
        one.setParent(parent);
        one.setResources(findResources(dto.getResourceIds()));
        one.setIconUrl(dto.getIconUrl());
        one.setAlias(dto.getAlias());
        one.setSortNum(dto.getSortNum());

        repository.save(one);
    }

    @Caching(evict = {
            @CacheEvict(key = SYS_HTML_COLLECTION),
            @CacheEvict(key = SYS_ZTREE_COLLECTION),
    })
    public void delete(Integer htmlId) {
        SystemHtml systemHtml = repository.findById(htmlId).orElse(null);
        if (systemHtml == null) return;
        Long count = repository.countByRoleBySystemHtmlId(systemHtml.getId());
        if (count != null && count > 0) {
            throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "已经有角色关联了该菜单，请先解绑");
        }
        repository.deleteAllByParent_Id(systemHtml.getId());
        repository.delete(systemHtml);
    }

    public List<SystemHtml> findByIdIn(Collection<Integer> htmlIds) {
        return repository.findByIdIn(htmlIds);
    }


    public SysHtmlDetailVO detail(Integer htmlId) {
        SystemHtml one = findOne(htmlId);
        return SysHtmlDetailVO.fromEntity(one);
    }

    private List<SystemResource> findResources(List<Long> resourceIds) {
        List<SystemResource> resources = new ArrayList<>(8);
        if (resourceIds == null) return resources;
        Map<Long, SystemResource> map = resourcesService.getAllResources()
                .stream()
                .collect(Collectors.toMap(SystemResource::getId, o -> o));
        resourceIds.forEach(id -> {
            SystemResource e = map.get(id);
            if (e == null) {
                throw new SystemServiceException(ErrorMessageEnum.RESOURCE_NOT_EXIST, "资源：" + id + " 不存在");
            }
            resources.add(e);
        });
        return resources;
    }

    private SystemHtml findOne(Integer htmlId) {
        return repository.findById(htmlId).orElseThrow(() -> new SystemServiceException(ErrorMessageEnum.HTML_NOT_EXIST));
    }


}
