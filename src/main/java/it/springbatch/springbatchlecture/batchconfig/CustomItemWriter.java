package it.springbatch.springbatchlecture.batchconfig;// package it.springbatch.springbatchlecture;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.batch.item.Chunk;
// import org.springframework.batch.item.ExecutionContext;
// import org.springframework.batch.item.ItemStreamException;
// import org.springframework.batch.item.ItemStreamWriter;
// import org.springframework.batch.item.ItemWriter;
// @Slf4j
// public class CustomItemWriter implements ItemStreamWriter<String> {
//
// 	@Override
// 	public void write(Chunk<? extends String> items) throws Exception {
// 		items.forEach(it-> log.info("it : {}", it));
// 	}
//
// 	@Override
// 	public void open(ExecutionContext executionContext) throws ItemStreamException {
// 		log.info("open");
// 	}
//
// 	@Override
// 	public void update(ExecutionContext executionContext) throws ItemStreamException {
// 		log.info("update");
// 	}
//
// 	@Override
// 	public void close() throws ItemStreamException {
// 		log.info("close");
// 	}
// }
