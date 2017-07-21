package io.appform.jsonrules;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appform.jsonrules.expressions.equality.EqualsExpression;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.expressions.preoperation.date.EpochOperation;
import io.appform.jsonrules.expressions.preoperation.numeric.AddOperation;
import io.appform.jsonrules.expressions.preoperation.numeric.DivideOperation;
import io.appform.jsonrules.expressions.preoperation.numeric.MultiplyOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.ppe on 19/07/17.
 */
public class PreoperationsTest {
    private ExpressionEvaluationContext context;
    private ObjectMapper mapper;
    private Instant dateTime;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        dateTime = Instant.now();
        long epoch = dateTime.getEpochSecond();
        String dateTimeStr = new StringBuilder().append("\"").append(dateTime.toString()).append("\"").toString();
        JsonNode node = mapper.readTree("{ \"value\": 20, \"string\" : \"Hello\", \"kid\": null, \"epochTime\" : 1500229740000, \"dateTime\" : "+dateTimeStr+" }");
        context = ExpressionEvaluationContext.builder().node(node).build();
    }

    @Test
    public void testWithMultiple() throws Exception {
        List<PreOperation<?>> preOperations = new ArrayList<>();
        preOperations.add(DivideOperation.builder().operand(2).build());
        preOperations.add(AddOperation.builder().operand(2).build());
        Assert.assertTrue(EqualsExpression.builder()
                .path("/value")
                .preoperations(preOperations)
                .value(12)
                .build()
                .evaluate(context));
    }

    @Test
    public void testWithEmpty() throws Exception {
        List<PreOperation<?>> preOperations = new ArrayList<>();
        Assert.assertTrue(EqualsExpression.builder()
                .path("/value")
                .preoperations(preOperations)
                .value(20)
                .build()
                .evaluate(context));
    }

    @Test
    public void testWithNull() throws Exception {
        Assert.assertTrue(EqualsExpression.builder()
                .path("/value")
                .preoperations(null)
                .value(20)
                .build()
                .evaluate(context));
    }

    @Test
    public void testWithDateAndNumeric() throws Exception {
        List<PreOperation<?>> preOperations = new ArrayList<>();
        preOperations.add(EpochOperation.builder().zoneOffSet("+05:30").operand("day_of_month").build());
        preOperations.add(AddOperation.builder().operand(4).build());
        Assert.assertTrue(EqualsExpression.builder()
                .path("/epochTime")
                .preoperations(preOperations)
                .value(20)
                .build()
                .evaluate(context));
    }
}
