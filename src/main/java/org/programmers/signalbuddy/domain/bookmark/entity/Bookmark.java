package org.programmers.signalbuddy.domain.bookmark.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;
import org.programmers.signalbuddy.domain.basetime.BaseTimeEntity;
import org.programmers.signalbuddy.domain.member.entity.Member;

@Entity(name = "bookmarks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @Column(nullable = false)
    private Point coordinate;

    @Column(nullable = false)
    private String address;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Bookmark(Point coordinate, String address, Member member) {
        this.coordinate = coordinate;
        this.address = address;
        this.member = member;
    }

    public void update(Point coordinate, String address) {
        if (coordinate != null) {
            this.coordinate = coordinate;
        }
        if (address != null) {
            this.address = address;
        }
    }
}