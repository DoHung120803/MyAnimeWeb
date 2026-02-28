package com.myanime.domain.service.jobs;

import com.myanime.common.constants.GlobalConstant;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.input.job.SyncUC;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.infrastructure.elasticsearch.ESDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("syncUserService")
@Slf4j
public class SyncUserService extends AbstractSyncService<UserModel> implements SyncUC {

    public SyncUserService(UserRepository userRepository, ESDocument esDocument) {
        super(userRepository, esDocument);
    }

    @Override
    protected String getIndexName() {
        return GlobalConstant.ESIndex.USERS;
    }

    @Override
    protected String getId(UserModel model) {
        return model.getId();
    }

    @Override
    protected Map<String, Object> toIndexMap(UserModel user) {
        Map<String, Object> mapData = new HashMap<>();

        mapData.put("id", user.getId());
        mapData.put("username", user.getUsername());
        mapData.put("fullName", user.getFirstName() + " " + user.getLastName());
        mapData.put("avtUrl", user.getAvtUrl());

        return mapData;
    }

    @Override
    protected String getEntityName() {
        return "users";
    }
}

