/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.connect.firstconnector.transform;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MoveTransformationRuleTest {

    @Test
    public void transform() {
        Map<String, Object> child = new HashMap<>();
        child.put("key", new HashMap<>());


        Map<String, Object> event = new HashMap<>();
        event.put("old_parent", child);
        event.put("new_parent", new HashMap<>());

        List<String> oldKey = new ArrayList<>();
        oldKey.add("old_parent");
        oldKey.add("key");

        List<String> newKey = new ArrayList<>();
        newKey.add("new_parent");


        MoveTransformationRule moveRule = new MoveTransformationRule(oldKey, newKey);

        Map<String, Object> result = moveRule.transform(event);

        assertEquals(2, result.keySet().size());
        assertEquals(0, ((Map<String, Object>) result.get("old_parent")).keySet().size());
        assertEquals(1, ((Map<String, Object>) result.get("new_parent")).keySet().size());
    }
}