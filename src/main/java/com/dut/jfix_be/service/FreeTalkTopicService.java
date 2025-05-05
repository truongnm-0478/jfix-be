package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.FreeTalkTopicRequest;
import com.dut.jfix_be.dto.response.FreeTalkTopicAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;

public interface FreeTalkTopicService {
    DataWithPageResponse<FreeTalkTopicAdminResponse> getAllFreeTalkTopicsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    FreeTalkTopicAdminResponse getFreeTalkTopicDetailForAdmin(Integer id);
    FreeTalkTopicAdminResponse createFreeTalkTopicForAdmin(FreeTalkTopicRequest request);
    FreeTalkTopicAdminResponse updateFreeTalkTopicForAdmin(Integer id, FreeTalkTopicRequest request);
    void deleteFreeTalkTopicForAdmin(Integer id);
}