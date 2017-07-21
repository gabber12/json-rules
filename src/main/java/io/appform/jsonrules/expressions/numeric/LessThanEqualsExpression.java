/*
 * Copyright (c) 2016 Santanu Sinha <santanu.sinha@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.appform.jsonrules.expressions.numeric;

import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.ExpressionType;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import lombok.Builder;

import java.util.List;

/**
 * Created by santanu on 15/9/16.
 */
public class LessThanEqualsExpression extends NumericJsonPathBasedExpression {
    public LessThanEqualsExpression() {
        super(ExpressionType.less_than_equals);
    }

    @Builder
    public LessThanEqualsExpression(String path, Number value, boolean defaultResult, List<PreOperation<?>> preoperations) {
        super(ExpressionType.less_than_equals, path, value, defaultResult, preoperations);
    }

    public LessThanEqualsExpression(String path, Number value, List<PreOperation<?>> preoperations) {
        this(path, value, false, preoperations);
    }

    protected boolean evaluate(ExpressionEvaluationContext context, int comparisonResult) {
        return comparisonResult <= 0;
    }

}
