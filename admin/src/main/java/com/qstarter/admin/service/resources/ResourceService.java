package com.qstarter.admin.service.resources;

import com.google.common.base.Strings;
import com.qstarter.admin.model.dto.ResourceDTO;
import com.qstarter.admin.model.dto.ResourceUpdateDTO;
import com.qstarter.admin.model.vo.ResourceVO;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.utils.PageView;
import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.service.ResourcePool;
import com.qstarter.security.service.SystemResourcesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-09 10:48
 **/
@Service
@Transactional
public class ResourceService {


    private SystemResourcesService systemResourcesService;

    public ResourceService(SystemResourcesService systemResourcesService) {
        this.systemResourcesService = systemResourcesService;
    }

    public ResourceVO add(ResourceDTO dto) {

        SystemResource resource = dto.convertToEntity();
        checkResourceExist(resource);

        return ResourceVO.fromEntity(systemResourcesService.add(resource));
    }

    public ResourceVO update(ResourceUpdateDTO dto) {
        SystemResource systemResource = dto.convertToEntity();
        checkResourceExist(systemResource);
        return ResourceVO.fromEntity(systemResourcesService.update(systemResource));
    }

    public void delete(Long resourceId) {
        ResourcePool.delete(resourceId);
        //删除
        systemResourcesService.delete(resourceId);
    }

    public PageView<ResourceVO> list(Integer pageIndex, Integer pageSize) {
        Page<SystemResource> page = systemResourcesService.findAllByPage(PageRequest.of(pageIndex, pageSize,  Sort.by(Sort.Direction.DESC, "createTime")));
        return PageView.fromPage(page, ResourceVO::fromEntity);
    }

    public List<ResourceVO> search(String search) {

        Collection<SystemResource> allResources = systemResourcesService.getAllResources();
        List<SystemResource> collect;
        if (!Strings.isNullOrEmpty(search)) {
            collect = allResources.stream().filter(r -> r.getResourceName().contains(search))
                    .collect(Collectors.toList());
        } else {
            collect = new ArrayList<>(allResources);
        }
        return collect.stream().map(ResourceVO::fromEntity).collect(Collectors.toList());
    }

    private void checkResourceExist(SystemResource resource) {
        boolean resourceExist = systemResourcesService.isResourceExist(resource.getResourceName(), resource.getOperation());

        if (resourceExist) {
            throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, resource.getResourceName() + "_" + resource.getOperation() + "已经存在");
        }
    }
}
