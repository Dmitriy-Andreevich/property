package space.meduzza.property.util;

import space.meduzza.property.model.UserEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

public class UserTestUtil {

    public static UserEntity createInstanceWithNewState() {
        return new UserEntity("mail@mail.com",
                              "pass",
                              "ROLE_TEST",
                              Collections.emptyList());
    }

    public static UserEntity createInstanceWithPersistentState(final long id) {
        UserEntity userEntity = new UserEntity("mail" + id + "@mail.com",
                                               "pass" + id,
                                               "ROLE_TEST",
                                               Collections.emptyList());
        userEntity.setId(id);
        userEntity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        userEntity.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        return userEntity;
    }
}
