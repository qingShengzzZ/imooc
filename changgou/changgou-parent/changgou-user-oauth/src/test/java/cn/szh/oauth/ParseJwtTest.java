package cn.szh.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {
    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.NvvKApSEfQMK9EY1dbtjLne-exwJwsa2CDWJaf6uKaJFj47WFjpgwQv582RhtdllRlUhWuiNITlRTtNcJnC-GYbILmnh2UZIV1AECHoBeqp0wvET1Pbi5IwcWwweSF1pikKlIc2_SPOqjdy4M9dMNKENXMyBfZrNrK5hKsBDvug2eUULSs_XWgZbyzh0ZFw9UC146ydJQx5USrWm5xEPINVy3CCitHMqGm5ewAW2F8Aqh3z_biAZshpxmLzs3ce1YToGn_DBUcRjo-0IJGY0YJZ7YigLFav96DswPpFQS-WM7t9vZdlyPj7bDBEjb6ofMVIWAXhAOZ2sDeOkF_lp4Q";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAojmhoZeNJueyCukt4thyFl1UHW2+V6I4VZF7lHR9Rt54g1eIWbe6XS3mv5AlSd3SlAkRGG/twtinlBglI2C8P2rhCFkKf5RndHI7FokjFDmmIJ27Ey43FwmEmJzKNxZIOBCvkv4tU9QZmeoQmadaoNpO6WHb8yEEG92yVuS2UMd/jDdqR3xmQXw+Oocdl84CdG03BDO/t+LYhhUw/CmsHxlA6fTvytCuu+ADMLt3nIzg7qSVakBgnbNjcQLmrlVwegtKzM3wJaAA/VmONyFv7XSWe/ZOfwgMnOYnETwL3wRZduSB1463Uwcx101CjtyfuWMS1WkXiAa1QO7d2EFRIQIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
