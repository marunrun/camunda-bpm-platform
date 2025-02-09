/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.batch.job;

import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.impl.batch.AbstractBatchJobHandler;
import org.camunda.bpm.engine.impl.batch.BatchJobContext;
import org.camunda.bpm.engine.impl.batch.BatchJobDeclaration;
import org.camunda.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.camunda.bpm.engine.impl.cmd.SetJobsRetriesCmd;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.MessageEntity;

import java.util.List;


/**
 * @author Askar Akhmerov
 */
public class SetJobRetriesJobHandler extends AbstractBatchJobHandler<SetRetriesBatchConfiguration> {
  public static final BatchJobDeclaration JOB_DECLARATION = new BatchJobDeclaration(Batch.TYPE_SET_JOB_RETRIES);

  @Override
  public String getType() {
    return Batch.TYPE_SET_JOB_RETRIES;
  }

  protected SetJobRetriesBatchConfigurationJsonConverter getJsonConverterInstance() {
    return SetJobRetriesBatchConfigurationJsonConverter.INSTANCE;
  }

  @Override
  public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
    return JOB_DECLARATION;
  }

  @Override
  protected SetRetriesBatchConfiguration createJobConfiguration(SetRetriesBatchConfiguration configuration, List<String> jobIds) {
    return new SetRetriesBatchConfiguration(jobIds, configuration.getRetries());
  }

  @Override
  public void executeHandler(SetRetriesBatchConfiguration batchConfiguration,
                             ExecutionEntity execution,
                             CommandContext commandContext,
                             String tenantId) {

    commandContext.executeWithOperationLogPrevented(
        new SetJobsRetriesCmd(batchConfiguration.getIds(), batchConfiguration.getRetries()));
  }
}
