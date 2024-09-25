package com.document.respond;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePagingResponse {

    private Integer page;
    private Long totalSize;
    private Integer totalPage;
    private Object data;

}
