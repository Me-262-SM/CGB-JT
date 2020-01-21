package com.jt.aop;


import com.jt.annotation.CacheFind;
import com.jt.util.JacksonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Component
@Aspect
public class CacheAOP {
//    @Autowired(required = false)
//    private ShardedJedis jedis;
//    @Autowired(required = false)
//    private JedisSentinelPool jedissentinelPool;
    @Autowired(required = false)
    private JedisCluster jedis;

    /**
     * 如果需要注解中的参数，切入点表达式中写注解名称
     * 如果不需要，则在切入点表达式中写入包名+注解名
     * <p>
     * key的生成策略 包名.类名.方法名::第一个参数parentId
     */
    @Around("@annotation(cacheFind)")
    public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
        Object obj = null;
        try {
            String key = getKey(joinPoint, cacheFind);
            String result = jedis.get(key);
            if (StringUtils.isEmpty(result)) {
                //第一次，执行目标方法
                System.out.println("查询数据库");
                obj = joinPoint.proceed();
                //将数据保存到redis中
                String json = JacksonUtils.toJson(obj);
                if(cacheFind.secomdes()>0)
                    jedis.setex(key,cacheFind.secomdes(),json);
                else
                    jedis.set(key,json);
            }else{
                //查询redis缓存
                //获取目标类型的返回值类型
                System.out.println("查询缓存");
                Class<?> returnType = getReturnType(joinPoint);
                obj = JacksonUtils.toPojo(result,returnType);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
        return obj;
    }


    private Class<?> getReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getReturnType();
    }


    /**
     * 指定了key，则用用户指定的值作为key
     * 如果没指定，则用自动生成的策略生成key
     */
    private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
        String key = cacheFind.key();
        if (!StringUtils.isEmpty(key)) {
            return key;
        }
        //方法API
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        long parentId = (long) args[0];
        String result = className + "." + methodName + "::" + parentId;
        System.out.println(result);
        return result;
    }

}
