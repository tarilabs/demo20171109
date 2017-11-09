/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.temp.demo20171109;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitTest {
    static final Logger LOG = LoggerFactory.getLogger(UnitTest.class);

    private static DMNRuntime dmnRuntime;
    private static DMNModel dmnModel;

    @BeforeClass
    public static void init() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }

        dmnRuntime = kContainer.newKieSession().getKieRuntime(DMNRuntime.class);

        dmnModel = dmnRuntime.getModel("http://www.signavio.com/dmn/1.1/diagram/da213bb354be4ec197ce1d0755322c75.xml", "Starts with an A");
    }

    @Test
    public void testTrue() {
        DMNContext dmnContext = dmnRuntime.newContext();
        dmnContext.set("surname", "Abc");

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        System.out.println(dmnResult);
        assertEquals(true, dmnResult.getDecisionResultByName("startsWithAnA").getResult());
    }

    @Test
    public void testFalse() {
        DMNContext dmnContext = dmnRuntime.newContext();
        dmnContext.set("surname", "Xyz");

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        System.out.println(dmnResult);
        assertEquals(false, dmnResult.getDecisionResultByName("startsWithAnA").getResult());
    }
}