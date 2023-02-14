package shop.mtcoding.blog.util;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UUIDTest {

    @Test
    public void uuid_test() throws Exception {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        // 해시에 대한 값으로 랜덤하게 뽑아준다.
        // 입력 문장의 길이에 관계없이 해시 알고리즘을 거쳐 일정한 길이의 값으로 변경(암호화) 된 것
        // 같은 값을 같은 해시 알고리즘에 실행하면 같은 해시값이 나온다.

        // 암호과는 가능하지만 복호화는 불가능함. 대신 한 번 해보았기 때문에 해시값을 보고 원래 값을 알 수 있다.
        // 똑같은 값을 넣어서 같은 해시값이 나온것을 확인해서 사용하는 것!

        // 해시라는 것은 데이터가 조금이라도 변경되면 다른 해시값이 나오기 때문에 정품인지 가품인지 바로 알 수 있다

        // 단, 일정한 길이기 때문에 값의 범위가 확률적임. 극악의 확률로 충돌날 수 있다.
    }
}
