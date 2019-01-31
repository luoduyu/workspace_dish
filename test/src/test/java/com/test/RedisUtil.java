package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtil {

    private @Autowired  StringRedisTemplate stringRedisTemplate;
    private @Autowired  RedisTemplate<String, Serializable> redisTemplate;


    private static String h = "demo.redis:h";

    public final static DateTimeFormatter DATE_COMPACT = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));

    public static final String WECHAT_SNAP_COUNT_CATE = "wmt_wechat:snapnum_cate:";

    @Test
    public void testAddHash(){
        String key = WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":1:"+"11:00:00".replaceAll(":","");
        stringRedisTemplate.opsForValue().increment(key,1);

        String key11 = WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":1:"+"12:00:00".replaceAll(":","");
        stringRedisTemplate.opsForValue().increment(key11,1);

        String key12 = WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":1:"+"13:00:00".replaceAll(":","");
        stringRedisTemplate.opsForValue().increment(key12,1);


        String key2 = WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":2:"+"11:00:00".replaceAll(":","");
        stringRedisTemplate.opsForValue().increment(key2,1);



        String key3 = WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":3:"+"11:00:00".replaceAll(":","");
        stringRedisTemplate.opsForValue().increment(key3,1);

//        redisTemplate.opsForHash().put(h+"1","1","aaa");
//        redisTemplate.opsForHash().put(h+"2","2","bbb");
//        redisTemplate.opsForHash().put(h+"3","3","ccc");
//        redisTemplate.opsForHash().put(h+"4","4","ddd");
    }
    //@Test
    public void testGetHash(){
        long size = redisTemplate.opsForHash().size(h);
        System.out.println("size="+size);

        Object hv1 = redisTemplate.opsForHash().get(h,"1");
        System.out.println("1="+hv1);

        Object hv2 = redisTemplate.opsForHash().get(h,"2");
        System.out.println("2="+hv2);


        redisTemplate.opsForHash().delete(h,"2");
        Object _hv2 = redisTemplate.opsForHash().get(h,"2");
        System.out.println("2="+_hv2);

        size = redisTemplate.opsForHash().size(h);
        System.out.println("size="+size);


        Map<Object,Object> entries = redisTemplate.opsForHash().entries(h);
        System.out.println(entries);

        redisTemplate.delete(h);

        Map<Object,Object> entries2 = redisTemplate.opsForHash().entries(h);
        System.out.println(entries2);
    }

    private static String ik = "demo.v";

    @Test
    public void testAddK(){
        for(int i=0;i<100;i++){
            stringRedisTemplate.opsForValue().set(ik+i,String.valueOf(i));
        }
    }

    @Test
    public void testScan(){


//        String key = WECHAT_SNAP_COUNT_CATE + "1"+":"+dateTimePostfix;
//
//        String dateTimePostfix = LocalDate.now().format(DATE_COMPACT)+"_";
//        String pattern = WECHAT_SNAP_COUNT_CATE + cateId+":"+dateTimePostfix;

        String pattern = WECHAT_SNAP_COUNT_CATE + "20190130:1:";
        scan(pattern);
    }

    private void scan(String prefix) {

        RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions().match(prefix+"*").build();
        Cursor<byte[]> cursor = connection.scan(options);
        int total = 0;

        //Map<String,String> result = new HashMap<>();
        while (cursor.hasNext()) {
            String key = new String(cursor.next());

            Object value = redisTemplate.opsForValue().get(key);
            //result.put(key,value);
            System.out.println(key.replace(prefix,"")+"="+value);
        }
        //System.out.println("total="+total);
        try {
            cursor.close();
        } catch (Exception e) {
            System.out.println("key="+prefix+",e="+e.getMessage());
        }
        try {
            System.out.println("connection.isClosed():"+connection.isClosed());
            RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory());
        } catch (Exception e) {
            System.out.println("key="+prefix+",e="+e.getMessage());
        }
        //return result;
    }
}
