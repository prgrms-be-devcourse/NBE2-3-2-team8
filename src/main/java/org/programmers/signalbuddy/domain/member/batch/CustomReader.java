package org.programmers.signalbuddy.domain.member.batch;

import java.util.ArrayList;
import java.util.List;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.springframework.batch.item.database.AbstractPagingItemReader;

public class CustomReader extends AbstractPagingItemReader<Member> {

    private final MemberBatchRepository memberBatchRepository;
    private final int pageSize;

    public CustomReader(MemberBatchRepository memberBatchRepository, int pageSize) {
        this.memberBatchRepository = memberBatchRepository;
        this.pageSize = pageSize;
    }

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new ArrayList<>();
        } else {
            results.clear();
        }

        List<Member> products = memberBatchRepository.findPageByMemberId(pageSize, 0);

        results.addAll(products);
    }

}
