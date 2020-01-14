package com.qstarter.core.thirdly_service.payment;

/**
 * @author peter
 * @version 1.0
 * @date 2019/04/03 19:34
 */
public interface PayConfigConstant {

    //TODO:

//        String HOST = "https://tourcoo.xiaomiqiu.com";
    String HOST = "http://47.104.168.189:8080/iov";//正式环境
//    String HOST = "http://119.3.20.53:8090/iov";//测试环境


    String BODY = "亿车聪-服务订单";

    interface WXConfig {
        /**
         * API接口秘钥
         */
        String key = "bWtEzXVXrolZ4iDF9lduIUNSJa5PMOq8";

        String appId = "wx54802f48398b86f9";

        String mchId = "1531899841";

        String baseNotifyUrl = "/api/public/pay/notify/wx";

        String notifyUrl = HOST + baseNotifyUrl;
    }

    interface AliConfig {

        String URL = "https://openapi.alipay.com/gateway.do";

        String appId = "2019040963848129";

        //私钥
        String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD1ZP6iGGLQGciKucqR4rmzx40XomHH9b58iPGQIpI1qA74pCddPNKT3nSH8lLIvxGL2GukEmSXuczDvHGk1ySmvXYs9tnsmvYkDCnFrvQ+eRMP6aaMg0B/QlYKGUCH9fEawO7abhCogle8VjMSwVlygv66gECzeM9Zu4gJIitmFHyiJ3vw5GvpxumeVLTzXOClbCesvKjp0HEtPmH44z4XUAo1iOwnm7Q4gLAQW0khrU7v2SigVYwX0R324D6oOcxjgJ5/3b3okuWM7ompaCbMY71s82g618RAH9ThlhTSRtCxv1pOU6yJkAUEB86Lvaoj8iPvvjDBiXpAsHhNo9YHAgMBAAECggEAE6pYTZ0urgai75scBLzqrOGdCPvWYjPrZAeFdEuQ9APXz004alxpc7gsFojw4W2OdmqNlcRzk0Ush7MeWFt1cHdWdRwDMtboStM+tbzjje4d/PR4iGIsVQZ35q9HYJ3xCHfVrGONqjNn3Iv749Di3i5pQ4DrrnZMB6DjsfevCqsJdXe88Wzu53Ln+JBSJmOxK/Qt5Dy90oN3hhC1iIef2G2erIuMfRJP4IAylEwibcMUDtTbGGbgB/qRRHi4q05ODDV4yjxHQniIk1bQDBwDKzNQs99ej9LipCIHjOe//OeZpVlz541Pj0lKTAQhADYsUaQ1ufavCQtr/fkVMoCKgQKBgQD8lh5zVzrXWHoNHZ2Zv2U4zljYyPk7V9BjA89bwwBfHYdG8KwXkcTv1mvtgl+xuOO64isH/6F1cHPuu7EvCj4vS/h3JCZoRfcnrEAei6a2xxRxCsDwZpNVr65T0BCtp/upy9ah9fsf8Ms+zTZyN2PlS183BJ9qSzmAUV7Do22Q8QKBgQD4tf5jNMUONlq7lADebyZGkNG1CRHm4UqhfK0Lm/7tyod9jtgZxUaitep+3+8s3Icy3RZYIkPDQtN4+lsRwrAXzXckcqvCGfEvv5DP+FFc86Qd0xVMY7J4wC3dJdUagj8iXg572rlMncvpPuNxkKM/fkaG12htLXezSVguJ0zWdwKBgCp/y8HWdIUVUvvv9ruO06Y6OgLH1f+hJ+PxGSu0ZNScME3EYwBYO42rQ1+kJxvNIywnr9vZrD4JXMhXdlmW3J2yp6zMPVYgJ5TaS7OYBcYNOyhGCe6pI7MwKyHZCWVetV59eLCppcZbCqeXeci//yPuiafFNFl7LMKlbk92qRHRAoGBANsSe8vozHZzXEKD4KwNXbqkj4OWRKWI5tMN3eMu+kN8tRHcw+8XPNvPv4kYOTXb3l7oLWlebS/OajXS2GrxgWjF+98n/rDPZwo3BBicUxa8KWB38PfDjY4dQYjdbTOXbIMaNEnWZIffKqD+WxtKWvFvijH2Ba69cQWEYl7tERqLAoGARpDQhqkPud5Qs20JYDAzsw6rd1KmUCD5Ejiwppi0WpfcZbYC1mjnqf4aZG2hQii/GwbcoLWZYmUdaRrQU0KeP4ZgmTFLTISKbY7TuMLZNDOneYYExqISntxSATSEe8MeNhbrXiko7W+KEJv97NwD5kkQPGXpZt44B4Mosvfq/b0=";
        /**
         * 公钥
         **/
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmQ/NnNHxmKsux9VgekRBHs3TXUgN3mnFX2gWMX+4YyCynOGSJ72nzHdgFtvplQ6H2JFAt9W7+9g9j2PpSmaCVRktjJ8vAvam9yphLFi38UMvnBRg3U1tX0uVqgyRmEQFgjBc8Vz7yUquHtLnnCj6GzJ/M4zS2FsmEhxCI+TdHbcJh4+6A7a7HWMsKpdW0GLJn0N620wgLFNWpEteExbrjZy9gD6Z2D2mg+IsMIA2Doh2TcsOOmAvA2N7VGwIsIWUJ0DF/YFbiLfCorEacaoE6EYWT/10i60lR4Y+wyy9LTIm9nX6pbWrbipdWS781RlMnCgTNZb6i7hQaBzX2KVkmwIDAQAB";

        String FORMAT = "json";

        String CHARSET = "UTF-8";

        String SIGN_TYPE = "RSA2";

        String baseNotifyUrl = "/api/public/pay/notify/ali";

        String notifyUrl = HOST + baseNotifyUrl;
    }

}
