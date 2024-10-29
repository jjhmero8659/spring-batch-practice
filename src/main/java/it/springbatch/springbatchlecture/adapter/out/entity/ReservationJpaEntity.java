package it.springbatch.springbatchlecture.adapter.out.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import kr.freezz.application.domain.model.UuidEntity;
import kr.freezz.application.model.ReservationStatus;
import kr.freezz.common.generator.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Table(name = "reservation", schema = "wegooli")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reservation SET deleted = true WHERE id=?")
@FilterDef(name = "deletedReservationFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedReservationFilter", condition = "deleted = :isDeleted")
public class ReservationJpaEntity extends UuidEntity {

	private byte[] vehicleId;
	private byte[] memberId;
	private Instant departureTime;
	private Instant arrivalTime;
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;


	public ReservationJpaEntity(byte[] id, byte[] vehicleId, byte[] memberId, Instant departureTime,
			Instant arrivalTime, ReservationStatus status,
			Instant createdAt,
			byte[] createdBy, Instant updatedAt, byte[] updatedBy, boolean deleted) {
		this.id = id;
		this.vehicleId = vehicleId;
		this.memberId = memberId;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.status = status;
		this.createdAt = createdAt;
		this.createdBy = IdGenerator.convertStringId(createdBy);
		this.updatedAt = updatedAt;
		this.updatedBy = IdGenerator.convertStringId(updatedBy);
		this.deleted = deleted;
	}

	// public ReservationStatus getStatus() {
	// 	return ReservationStatus.fromCode(status);
	// }
}