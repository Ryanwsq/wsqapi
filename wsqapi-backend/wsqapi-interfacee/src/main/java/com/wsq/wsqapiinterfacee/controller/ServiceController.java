package com.wsq.wsqapiinterfacee.controller;

import cn.hutool.json.JSONUtil;
import com.wsq.wsqapiclientsdk.model.User;
import icu.qimuu.qiapisdk.exception.ApiException;
import icu.qimuu.qiapisdk.model.params.RandomWallpaperParams;
import icu.qimuu.qiapisdk.model.response.RandomWallpaperResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wsq.wsqapiinterfacee.utils.ResponseUtils.buildUrl;
import static com.wsq.wsqapiinterfacee.utils.ResponseUtils.get;

/**
 * @Author: wsq
 * @Date: 2024/3/1 15:25
 */
@RestController
@RequestMapping("/")
public class ServiceController {
    @PostMapping("/name/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        String result = "Post 用户名字是" + user.getUsername();
        return result;
    }

    /**
     * 2. 随机毒鸡汤
     *
     * @return
     */
    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");// 真实的第三方接口地址
    }

    /**
     * 3. 随机壁纸
     *
     * @param randomWallpaperParams
     * @return
     * @throws ApiException
     */
    @GetMapping("/randomWallpaper")
    public RandomWallpaperResponse randomWallpaper(RandomWallpaperParams randomWallpaperParams) throws ApiException {
//        String baseUrl = "https://api.btstu.cn/sjbz/api.php";
        String baseUrl = "https://api.vvhan.com/api/view?type=json";
        String url = buildUrl(baseUrl, randomWallpaperParams);
        if (StringUtils.isAllBlank(randomWallpaperParams.getLx(), randomWallpaperParams.getMethod())) {
            url = url + "?format=json";
        } else {
            url = url + "&format=json";
        }
        return JSONUtil.toBean(get(baseUrl), RandomWallpaperResponse.class);
    }

    /**
     * 4. 随机土味情话
     *
     * @return
     */
    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return get("https://api.vvhan.com/api/love");
    }

    /**
     * 5. 每日一句励志英语
     *
     * @return
     */
    @GetMapping("/en")
    public String dailyEnglish() {
        return get("https://api.vvhan.com/api/en");
    }

    /**
     * 6. 随机笑话
     *
     * @return
     */
    @GetMapping("/joke")
    public String randomJoke() {
        return get("https://api.vvhan.com/api/joke");
    }



}
