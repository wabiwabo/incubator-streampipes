/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.model.connect.rules;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.fogsy.empire.annotations.Namespaces;
import io.fogsy.empire.annotations.RdfsClass;
import org.apache.streampipes.model.base.UnnamedStreamPipesEntity;
import org.apache.streampipes.model.connect.rules.schema.SchemaTransformationRuleDescription;
import org.apache.streampipes.model.connect.rules.stream.StreamTransformationRuleDescription;
import org.apache.streampipes.model.connect.rules.value.ValueTransformationRuleDescription;
import org.apache.streampipes.vocabulary.StreamPipes;

import javax.persistence.Entity;

@Namespaces({StreamPipes.NS_PREFIX, StreamPipes.NS})
@RdfsClass(StreamPipes.TRANSFORM_RULE_DESCRIPTION)
@Entity
@JsonSubTypes({
        @JsonSubTypes.Type(ValueTransformationRuleDescription.class),
        @JsonSubTypes.Type(StreamTransformationRuleDescription.class),
        @JsonSubTypes.Type(SchemaTransformationRuleDescription.class),
})
public abstract class TransformationRuleDescription extends UnnamedStreamPipesEntity {


    public TransformationRuleDescription() {
        super();
    }

    public TransformationRuleDescription(TransformationRuleDescription other) {
        super();
    }
}
