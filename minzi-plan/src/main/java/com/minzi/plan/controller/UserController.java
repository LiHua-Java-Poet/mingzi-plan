package com.minzi.plan.controller;


import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.user.UserLoginTo;
import com.minzi.plan.model.vo.user.UserLoginVo;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/app/user")
@Api(tags = "用户管理")
public class UserController {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 50;
    private static final int CODE_LEN = 6;

    @Resource
    private UserService userService;

    @ApiOperation(value = "获取到用户列表", response = UserLoginTo.class)
    @GetMapping("/getUserList")
    public R getUserList() {
        List<UserEntity> list = userService.getList();
        return R.ok().setData(list);
    }

    @PostMapping("/reg")
    public R reg(@RequestBody UserRegVo vo) {
        userService.reg(vo);
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo vo) {
        return userService.login(vo.getUserName(), vo.getPassword());
    }


    @GetMapping("/image")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {

        // 1. 生成随机验证码
        String code = generateCode(CODE_LEN);

        // 2. 保存验证码（后续校验用）
        session.setAttribute("CAPTCHA_CODE", code);

        // 3. 创建图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 4. 背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 5. 抗锯齿
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // 6. 干扰线
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            g.setColor(getRandomColor());
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 7. 写验证码字符
        g.setFont(new Font("Arial", Font.BOLD, 32));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(getRandomColor());
            int x = 20 + i * 22;
            int y = 35 + random.nextInt(5);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
        }

        // 8. 干扰点
        for (int i = 0; i < 100; i++) {
            g.setColor(getRandomColor());
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g.fillRect(x, y, 1, 1);
        }

        g.dispose();

        // 9. 输出图片
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache");
        ImageIO.write(image, "png", response.getOutputStream());
    }

    // 随机验证码
    private String generateCode(int length) {
        String chars = "abcdefghjkmnpqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // 随机颜色
    private Color getRandomColor() {
        Random random = new Random();
        return new Color(
                random.nextInt(150),
                random.nextInt(150),
                random.nextInt(150)
        );
    }

}
