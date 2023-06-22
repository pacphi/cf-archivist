package io.pivotal.cfapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pivotal.cfapp.config.GitSettings;
import io.pivotal.cfapp.domain.Policies;
import io.pivotal.cfapp.repository.R2dbcPoliciesRepository;
import reactor.core.publisher.Mono;

@Service
public class R2dbcPoliciesService implements PoliciesService {

    private static final String UNSUPPORTED_OP_MESSAGE = "Policies are managed in a git repository.";

    private final R2dbcPoliciesRepository repo;
    private final GitSettings settings;

    public R2dbcPoliciesService(
            R2dbcPoliciesRepository repo,
            GitSettings settings) {
        this.repo = repo;
        this.settings = settings;
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll() {
        return repo.deleteAll();
    }

    @Override
    @Transactional
    public Mono<Void> deleteQueryPolicyById(String id) {
        if (settings.isVersionManaged()) {
            throw new UnsupportedOperationException(UNSUPPORTED_OP_MESSAGE);
        }
        return repo.deleteQueryPolicyById(id);
    }

    @Override
    public Mono<Policies> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Policies> findAllQueryPolicies() {
        return repo.findAllQueryPolicies();
    }

    @Override
    public Mono<Policies> findQueryPolicyById(String id) {
        return repo.findQueryPolicyById(id);
    }

    @Override
    @Transactional
    public Mono<Policies> save(Policies entity) {
        return repo.save(entity);
    }

}
