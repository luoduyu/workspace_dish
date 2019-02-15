package com.wmt.dlock.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.Nullable;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-15 19:11
 * @version 1.0
 */
@Component("myRedisScript")
public class MyRedisScript implements RedisScript<String>, InitializingBean {

    private static final String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private @Nullable ScriptSource scriptSource;
    private @Nullable String sha1;
    private @Nullable Class<String> resultType = String.class;

    public MyRedisScript(){
        this.sha1 = DigestUtils.sha1DigestAsHex(luaScript);
        this.scriptSource = new StaticScriptSource(luaScript);;
    }


    /**
     * @return The SHA1 of the script, used for executing Redis evalsha command.
     */
    @Override
    public String getSha1(){
        return sha1;
    }

    /**
     * @return The script result type. Should be one of Long, Boolean, List, or deserialized value type. {@literal null}
     *         if the script returns a throw-away status (i.e "OK").
     */
    @Nullable
    @Override
    public Class<String> getResultType(){
        return resultType;
    }

    /**
     * @return The script contents.
     */
    @Override
    public String getScriptAsString(){
        return luaScript;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(this.scriptSource != null, "Either script, script location," + " or script source is required");
    }
}
