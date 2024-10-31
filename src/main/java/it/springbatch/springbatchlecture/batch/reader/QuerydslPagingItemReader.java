package it.springbatch.springbatchlecture.batch.reader;

import static it.springbatch.springbatchlecture.adapter.out.entity.QReservationJpaEntity.reservationJpaEntity;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import it.springbatch.springbatchlecture.adapter.out.entity.ReservationJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import kr.freezz.common.generator.IdGenerator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

@Slf4j
public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

	protected final Map<String, Object> jpaPropertyMap = new HashMap<>();
	protected EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	protected Function<JPAQueryFactory, JPAQuery<T>> queryFunction;
	@Setter
	protected boolean transacted = false; // default value = true
	private byte[] lastId;

	protected QuerydslPagingItemReader() {
		setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
	}

	public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory,
			int pageSize, Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		this(entityManagerFactory, pageSize, true, queryFunction);
	}

	public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory,
			boolean transacted,
			Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		this();
		this.entityManagerFactory = entityManagerFactory;
		this.queryFunction = queryFunction;
		setTransacted(transacted);
	}

	public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory,
			int pageSize,
			boolean transacted,
			Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		this();
		this.entityManagerFactory = entityManagerFactory;
		this.queryFunction = queryFunction;
		setPageSize(pageSize);
		setTransacted(transacted);
	}


	@Override
	protected void doOpen() throws Exception {
		super.doOpen();
		entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doReadPage() {
		clearIfTransacted();

		log.info("last ID : {}", IdGenerator.convertStringId(lastId));

		JPQLQuery<T> query = createQuery()
				.where(
						lastId != null ?
								Expressions.stringTemplate("HEX({0})", reservationJpaEntity.id)
										.gt(Expressions.stringTemplate("HEX({0})", lastId))
								: null)
				.limit(getPageSize());

		initResults();

		fetchQuery(query);

		if (!results.isEmpty()) {
			T lastEntity = results.get(results.size() - 1);
			lastId = extractIdFromEntity(lastEntity);
		}
	}

	protected byte[] extractIdFromEntity(T entity) {
		return ((ReservationJpaEntity) entity).getId();
	}

	protected void clearIfTransacted() {
		if (transacted) {
			entityManager.clear();
		}
	}

	protected JPAQuery<T> createQuery() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		return queryFunction.apply(queryFactory);
	}

	protected void initResults() {
		if (CollectionUtils.isEmpty(results)) {
			results = new CopyOnWriteArrayList<>();
		} else {
			results.clear();
		}
	}

	protected void fetchQuery(JPQLQuery<T> query) {
		if (transacted) {
			results.addAll(query.fetch());
			// if (tx != null) {
			// 	tx.commit();
			// }
		} else {
			List<T> queryResult = query.fetch();
			for (T entity : queryResult) {
				entityManager.detach(entity);
				results.add(entity);
			}
		}
	}

	@Override
	protected void jumpToItem(int itemIndex) {
	}

	@Override
	protected void doClose() throws Exception {
		entityManager.close();
		super.doClose();
	}
}