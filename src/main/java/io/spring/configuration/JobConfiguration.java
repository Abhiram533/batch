/*
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.configuration;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Minella
 */
@Configuration
public class JobConfiguration {

	private static final Log logger = LogFactory.getLog(JobConfiguration.class);
	private static final int DEFAULT_CHUNK_COUNT = 3;
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step step2() {
		return this.stepBuilderFactory.get("step2")
			.<String, String>chunk(DEFAULT_CHUNK_COUNT)
			.reader(new ListItemReader<>(Arrays.asList("1", "2", "3", "4", "5", "8")))
			.processor(new ItemProcessor<String, String>() {
				@Override
				public String process(String item) throws Exception {
					//return String.valueOf(Integer.parseInt(item)*-1);
					return String.valueOf(null);
				}
			})
			.writer(new ItemWriter<String>() {
				@Override
				public void write(List<? extends String> items) throws Exception {
					for (String item : items) {
						System.out.println(">> " + item);
					}
				}
			}).build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job")
			.start(step2())
			//.next(step2())
			.build();
	}

}
