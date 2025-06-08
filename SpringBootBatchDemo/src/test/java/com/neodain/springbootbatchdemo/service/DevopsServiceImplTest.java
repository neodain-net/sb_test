package com.neodain.springbootbatchdemo.service;

import com.neodain.springbootbatchdemo.dto.DevopsDto.DevopsRequest;
import com.neodain.springbootbatchdemo.dto.DevopsDto.DevopsResponse;
import com.neodain.springbootbatchdemo.service.impl.DevopsServiceImpl;
import com.neodain.springbootbatchdemo.store.jpo.Devops;
import com.neodain.springbootbatchdemo.store.repository.IDevopsRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DevopsServiceImpl.class)
@DisplayName("DevopsServiceImpl DataJpaTest")
class DevopsServiceImplTest {

    @Autowired
    private DevopsServiceImpl service;
    
    @Autowired
    private IDevopsRepository repository;

    @Test
    @DisplayName("create devops")
    void create() {
        DevopsRequest request = new DevopsRequest("team", "intro");
        DevopsResponse response = service.create(request);

        assertThat(response.devopsId()).isNotNull();
        assertThat(repository.count()).isEqualTo(1);
        Devops saved = repository.findById(response.devopsId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("team");
    }

    @Test
    @DisplayName("get all devops")
    void getAll() {
        service.create(new DevopsRequest("team1", "i1"));
        service.create(new DevopsRequest("team2", "i2"));

        List<DevopsResponse> all = service.getAll();
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("delete devops")
    void delete() {
        DevopsResponse response = service.create(new DevopsRequest("team", "intro"));
        service.delete(response.devopsId());
        assertThat(repository.findById(response.devopsId())).isEmpty();
    }
}