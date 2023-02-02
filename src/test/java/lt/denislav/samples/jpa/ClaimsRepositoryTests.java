/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lt.denislav.samples.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.Policy;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import lt.denislav.samples.jpa.repository.PolicyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClaimsRepositoryTests {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ClaimsRepository claimsRepository;

	@Autowired
	private PolicyRepository policyRepository;

	@Test
	@Transactional
	public void testFindByLastName() {
		Policy policy = new Policy();
		policy.setPolicyNumber("A01");

		Claim claim1 = new Claim();
		claim1.setPolicy(policy);

		Claim claim2 = new Claim();
		claim2.setPolicy(policy);

		policy.getClaims().add(claim1);
		policy.getClaims().add(claim2);

		policyRepository.save(policy);

		//entityManager.persist(policy);

		/*entityManager.flush();
		entityManager.clear();

		policy = entityManager.find(Policy.class, policy.getId());*/



		//assertThat(policy.getClaims().size(), equalTo(2));
	}
}