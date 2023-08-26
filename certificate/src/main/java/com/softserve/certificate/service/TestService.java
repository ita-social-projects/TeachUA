package com.softserve.certificate.service;

import com.softserve.certificate.model.TestModel;

public interface TestService {
    void save(TestModel data);

    void delete(Long id);

    void restore(Long id);
}
