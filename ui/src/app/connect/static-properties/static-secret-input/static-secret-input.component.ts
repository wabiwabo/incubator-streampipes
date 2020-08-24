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

import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Validators} from '@angular/forms';
import {StaticPropertyUtilService} from '../static-property-util.service';
import {ConfigurationInfo} from "../../model/ConfigurationInfo";
import {SecretStaticProperty} from "../../../core-model/gen/streampipes-model";
import {AbstractValidatedStaticPropertyRenderer} from "../base/abstract-validated-static-property";

@Component({
    selector: 'app-static-secret-input',
    templateUrl: './static-secret-input.component.html',
    styleUrls: ['./static-secret-input.component.css']
})
export class StaticSecretInputComponent
    extends AbstractValidatedStaticPropertyRenderer<SecretStaticProperty> implements OnInit {

    constructor(public staticPropertyUtil: StaticPropertyUtilService){
        super();
    }

    @Output() updateEmitter: EventEmitter<ConfigurationInfo> = new EventEmitter();

    ngOnInit() {
        this.addValidator(this.staticProperty.value, Validators.required);
        this.enableValidators();
    }


    emitUpdate() {
        this.updateEmitter.emit(new ConfigurationInfo(this.staticProperty.internalName, this.staticPropertyUtil.asFreeTextStaticProperty(this.staticProperty).value && this.staticPropertyUtil.asFreeTextStaticProperty(this.staticProperty).value !== ""));
    }

    onStatusChange(status: any) {
    }

    onValueChange(value: any) {
        this.staticProperty.value = value;
        this.staticProperty.encrypted=false;
    }

}