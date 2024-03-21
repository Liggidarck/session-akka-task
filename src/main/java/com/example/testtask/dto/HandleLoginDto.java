package com.example.testtask.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class HandleLoginDto {

    private LoginDto loginDto;
    private HttpServletRequest request;
    private HttpServletResponse response;
}
