package com.zhaopei.train.member.controller;


import com.zhaopei.train.common.resp.CommonResp;
import com.zhaopei.train.member.req.MemberLoginReq;
import com.zhaopei.train.member.req.MemberRegisterReq;
import com.zhaopei.train.member.req.MemberSendCodeReq;
import com.zhaopei.train.member.resp.MemberLoginResp;
import com.zhaopei.train.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")

public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count(){
        int count=memberService.count();
//        CommonResp<Integer> commonResp=new CommonResp<>();
//        commonResp.setContent(count);
//        return commonResp;
        return new CommonResp<>(count);

    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq req){
        long register = memberService.register(req);
//        CommonResp<Long> commonResp=new CommonResp<>();
//        commonResp.setContent(register);
//        return commonResp;
        return new CommonResp<>(register);
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req){
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid MemberLoginReq req){
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<>(resp);
    }
}
