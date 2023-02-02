package lt.denislav.samples.jpa;

import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamTest {

    @Autowired
    private ClaimsRepository claimsRepository;

    @Before
    public void saveTenClaims() {
        for (int i = 0; i < 10; i++) {
            Claim claim = claimsRepository.save(new Claim());
        }
    }

    @Test
    @Transactional
    public void fetch() {
        Stream<Claim> claimList = claimsRepository.getAll();

        claimList.forEach(claim -> System.err.println(claim));

        System.err.println(claimList);
    }
}
