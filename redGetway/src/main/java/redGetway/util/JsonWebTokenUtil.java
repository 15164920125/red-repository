package redGetway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.lang.Assert;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.*;

/**
 * @author sundonghao
 * @date 16:29 2020/3/15
 */
public class JsonWebTokenUtil {

    public static final String SECRET_KEY = "?::4343fdf4fdf6cvf):";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int COUNT_2 = 2;
    private static  CompressionCodecResolver codecResolver = new DefaultCompressionCodecResolver();
    private static long refreshPeriodTime = 60000L;
    /**
     *   json web token 签发
     * @param id 令牌ID
     * @param subject 用户ID
     * @param issuer 签发人
     * @param period 有效时间(毫秒)
     * @param roles 访问主张-角色
     * @param permissions 访问主张-权限
     * @param algorithm 加密算法
     * @return java.lang.String
     */
    public static String issueJWT(String id,String subject, String issuer, String roles, String permissions, SignatureAlgorithm algorithm) {
        // 当前时间戳
        Long currentTimeMillis = System.currentTimeMillis();
        // 秘钥
        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("appId", subject);
        claims.put("roles", roles);
        claims.put("perms", permissions);
        
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder jwtBuilder = Jwts.builder();
        //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
        jwtBuilder.setClaims(claims)
        //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击
        .setId(id)
        //iat: jwt的签发时间
        .setIssuedAt(new Date(currentTimeMillis))
        //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
        .setSubject(subject);
        //设置过期时间
        jwtBuilder.setExpiration(new Date(currentTimeMillis+refreshPeriodTime*60));
        // 压缩，可选GZIP
        jwtBuilder.compressWith(CompressionCodecs.DEFLATE);
        //设置签名使用的签名算法和签名使用的秘钥
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secreKeyBytes);

        return jwtBuilder.compact();
    }

    /**
     * 解析JWT的Payload
     */
    public static String parseJwtPayload(String jwt){
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                CharSequence tokenSeq = io.jsonwebtoken.lang.Strings.clean(sb);
                String token = tokenSeq!=null?tokenSeq.toString():null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != COUNT_2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }
        // =============== Header =================
        Header header = null;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            compressionCodec = codecResolver.resolveCompressionCodec(header);
        }
        // =============== Body =================
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, io.jsonwebtoken.lang.Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }
        return payload;
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static JwtAccount parseJwt(String jwt, String appKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(appKey))
                .parseClaimsJws(jwt)
                .getBody();
        JwtAccount jwtAccount = new JwtAccount();
        //令牌ID
        jwtAccount.setTokenId(claims.getId());
        // 客户标识
        jwtAccount.setAppId(claims.getSubject());
        // 签发者
        jwtAccount.setIssuer(claims.getIssuer());
        // 签发时间
        jwtAccount.setIssuedAt(claims.getIssuedAt());
        // 过期时间
        jwtAccount.setExpiration(claims.getExpiration());
        // 接收方
        jwtAccount.setAudience(claims.getAudience());
        // 访问主张-角色
        jwtAccount.setRoles(claims.get("roles", String.class));
        // 访问主张-权限
        jwtAccount.setPerms(claims.get("perms", String.class));
        return jwtAccount;
    }


    /**
     * description 从json数据中读取格式化map
     *
     * @param val 1
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

    /**
     * 分割字符串进SET
     */
    @SuppressWarnings("unchecked")
    public static Set<String> split(String str) {

        Set<String> set = new HashSet<>();
        if (StringUtils.isEmpty(str)) {
            return set;
        }
        set.addAll(CollectionUtils.arrayToList(str.split(",")));
        return set;
    }

}
