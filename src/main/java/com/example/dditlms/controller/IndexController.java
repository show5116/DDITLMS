package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Todo;
import com.example.dditlms.domain.repository.TodoRepository;
import com.example.dditlms.service.MemberService;
import com.example.dditlms.service.TodoService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final MemberService memberService;

    private final TodoService todoService;

    private final TodoRepository todoRepository;

    @ResponseBody
    @PostMapping("/getOrder")
    public String getOrder(HttpServletResponse response){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        String[] orders = MemberUtil.getLoginMember().getOrder().split(" ");
        JSONArray jsonArray = new JSONArray();
        for(String order : orders){
            jsonArray.add(order);
        }
        jsonObject.put("orderList",jsonArray);
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/setOrder")
    public String setOrder(@RequestBody Map<String,List<String>> paramMap){
        JSONObject jsonObject = new JSONObject();
        List<String> orderList = paramMap.get("orderList");
        StringBuilder sb = new StringBuilder();
        for(String order : orderList){
            sb.append(order);
            sb.append(" ");
        }
        Member member = MemberUtil.getLoginMember();
        member.setOrder(sb.toString());
        memberService.changeData(member);
        jsonObject.put("success","ok");
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/getTodo")
    public String getTodo(HttpServletResponse response){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        List<Todo> todoList = todoRepository.findAllByMember(MemberUtil.getLoginMember());
        JSONArray jsonArray = new JSONArray();
        for(Todo todo : todoList){
            JSONObject todoJson = new JSONObject();
            todoJson.put("id",todo.getId());
            todoJson.put("content",todo.getContent());
            todoJson.put("status",todo.getTodoStatus()+"");
            jsonArray.add(todoJson);
        }
        jsonObject.put("todoList",jsonArray);
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/deleteTodo")
    public String deleteTodo(@RequestParam long id){
        JSONObject jsonObject = new JSONObject();
        todoService.deleteTodo(id);
        return  jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/updateTodo")
    public String updateTodo(@RequestParam long id){
        JSONObject jsonObject = new JSONObject();
        todoService.checkTodo(id);
        return  jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/saveTodo")
    public String saveTodo(@RequestParam String content){
        JSONObject jsonObject = new JSONObject();
        Todo todo = Todo.builder()
                .content(content)
                .member(MemberUtil.getLoginMember())
                .build();
        todoService.saveTodo(todo);
        jsonObject.put("id",todo.getId());
        return jsonObject.toJSONString();
    }
}
