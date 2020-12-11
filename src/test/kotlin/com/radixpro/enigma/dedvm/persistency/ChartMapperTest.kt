package com.radixpro.enigma.dedvm.persistency

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ChartMapperTest {

    private val delta = 0.00000001

    private val json = " {\n" +
            "  \"name\" : \"data_long_calculatedCharts.json\",\n" +
            "  \"creation\" : \"2020-12-11T15:59:01.359987\",\n" +
            "  \"charts\" : [ {\n" +
            "    \"id\" : \"395\",\n" +
            "    \"name\" : \"Oswald Spengler\",\n" +
            "    \"location\" : {\n" +
            "      \"geoLat\" : 51.8,\n" +
            "      \"geoLon\" : 10.966666666666667\n" +
            "    },\n" +
            "    \"jdUt\" : 2407866.5507639353,\n" +
            "    \"armc\" : 278.1586752091767,\n" +
            "    \"epsilon\" : 23.455119947999492,\n" +
            "    \"dateTimeParts\" : {\n" +
            "      \"year\" : 1880,\n" +
            "      \"month\" : 5,\n" +
            "      \"day\" : 29,\n" +
            "      \"hour\" : 49,\n" +
            "      \"minute\" : 56,\n" +
            "      \"second\" : 58,\n" +
            "      \"offsetUt\" : 0.73111\n" +
            "    },\n" +
            "    \"pointPositions\" : [ {\n" +
            "      \"point\" : \"SUN\",\n" +
            "      \"lon\" : 69.91846748687968,\n" +
            "      \"speed\" : 0.9578737337673087\n" +
            "    }, {\n" +
            "      \"point\" : \"MOON\",\n" +
            "      \"lon\" : 341.1061286660732,\n" +
            "      \"speed\" : 13.17738732354736\n" +
            "    }, {\n" +
            "      \"point\" : \"MERCURY\",\n" +
            "      \"lon\" : 66.88859704033467,\n" +
            "      \"speed\" : 2.1853045701026432\n" +
            "    }, {\n" +
            "      \"point\" : \"VENUS\",\n" +
            "      \"lon\" : 57.93066955704065,\n" +
            "      \"speed\" : 1.2243681776135589\n" +
            "    }, {\n" +
            "      \"point\" : \"MARS\",\n" +
            "      \"lon\" : 118.92699945072201,\n" +
            "      \"speed\" : 0.5981153452758893\n" +
            "    }, {\n" +
            "      \"point\" : \"JUPITER\",\n" +
            "      \"lon\" : 12.561941530300626,\n" +
            "      \"speed\" : 0.18059153754774543\n" +
            "    }, {\n" +
            "      \"point\" : \"SATURN\",\n" +
            "      \"lon\" : 24.964243818587082,\n" +
            "      \"speed\" : 0.10166634021797052\n" +
            "    }, {\n" +
            "      \"point\" : \"URANUS\",\n" +
            "      \"lon\" : 155.0140887844541,\n" +
            "      \"speed\" : 0.016779165995559845\n" +
            "    }, {\n" +
            "      \"point\" : \"NEPTUNE\",\n" +
            "      \"lon\" : 42.81855333123575,\n" +
            "      \"speed\" : 0.03428239174955583\n" +
            "    }, {\n" +
            "      \"point\" : \"PLUTO\",\n" +
            "      \"lon\" : 57.18898311509757,\n" +
            "      \"speed\" : 0.022118767193265696\n" +
            "    }, {\n" +
            "      \"point\" : \"MEAN_NODE\",\n" +
            "      \"lon\" : 277.9904668753706,\n" +
            "      \"speed\" : -0.05297181889784969\n" +
            "    }, {\n" +
            "      \"point\" : \"MEAN_APOGEE\",\n" +
            "      \"lon\" : 77.48607277664931,\n" +
            "      \"speed\" : 0.11088685914598435\n" +
            "    }, {\n" +
            "      \"point\" : \"CHIRON\",\n" +
            "      \"lon\" : 0.0,\n" +
            "      \"speed\" : 0.0\n" +
            "    } ],\n" +
            "    \"cusps\" : [ 0.0, 19.431628924939407, 58.617917849615814, 79.96755674747885, 97.49253858903239, 116.47511867749006, 143.95744310257578, 199.43162892493942, 238.61791784961582, 259.96755674747885, 277.4925385890324, 296.47511867749006, 323.9574431025758 ]\n" +
            "  }, {\n" +
            "    \"id\" : \"396\",\n" +
            "    \"name\" : \"Hermann Keyserling\",\n" +
            "    \"location\" : {\n" +
            "      \"geoLat\" : 58.88333333333333,\n" +
            "      \"geoLon\" : 24.833333333333332\n" +
            "    },\n" +
            "    \"jdUt\" : 2407918.5492824074,\n" +
            "    \"armc\" : 342.744706707277,\n" +
            "    \"epsilon\" : 23.454983310308464,\n" +
            "    \"dateTimeParts\" : {\n" +
            "      \"year\" : 1880,\n" +
            "      \"month\" : 7,\n" +
            "      \"day\" : 20,\n" +
            "      \"hour\" : 50,\n" +
            "      \"minute\" : 49,\n" +
            "      \"second\" : 58,\n" +
            "      \"offsetUt\" : 1.65\n" +
            "    },\n" +
            "    \"pointPositions\" : [ {\n" +
            "      \"point\" : \"SUN\",\n" +
            "      \"lon\" : 119.54973393244804,\n" +
            "      \"speed\" : 0.9545575190328771\n" +
            "    }, {\n" +
            "      \"point\" : \"MOON\",\n" +
            "      \"lon\" : 301.9477691418178,\n" +
            "      \"speed\" : 14.832070282428347\n" +
            "    }, {\n" +
            "      \"point\" : \"MERCURY\",\n" +
            "      \"lon\" : 140.13543274519733,\n" +
            "      \"speed\" : 0.03406375426875018\n" +
            "    }, {\n" +
            "      \"point\" : \"VENUS\",\n" +
            "      \"lon\" : 121.76828359506953,\n" +
            "      \"speed\" : 1.2318387860182096\n" +
            "    }, {\n" +
            "      \"point\" : \"MARS\",\n" +
            "      \"lon\" : 150.6463860353412,\n" +
            "      \"speed\" : 0.6216829060680484\n" +
            "    }, {\n" +
            "      \"point\" : \"JUPITER\",\n" +
            "      \"lon\" : 19.047435185234995,\n" +
            "      \"speed\" : 0.056571947807966996\n" +
            "    }, {\n" +
            "      \"point\" : \"SATURN\",\n" +
            "      \"lon\" : 28.635948687436315,\n" +
            "      \"speed\" : 0.033098484017235544\n" +
            "    }, {\n" +
            "      \"point\" : \"URANUS\",\n" +
            "      \"lon\" : 156.91751421875858,\n" +
            "      \"speed\" : 0.0528806291719964\n" +
            "    }, {\n" +
            "      \"point\" : \"NEPTUNE\",\n" +
            "      \"lon\" : 44.14384083336152,\n" +
            "      \"speed\" : 0.014158912492577468\n" +
            "    }, {\n" +
            "      \"point\" : \"PLUTO\",\n" +
            "      \"lon\" : 58.13341873817537,\n" +
            "      \"speed\" : 0.01224207307177373\n" +
            "    }, {\n" +
            "      \"point\" : \"MEAN_NODE\",\n" +
            "      \"lon\" : 275.2375022179935,\n" +
            "      \"speed\" : -0.052932045504457674\n" +
            "    }, {\n" +
            "      \"point\" : \"MEAN_APOGEE\",\n" +
            "      \"lon\" : 83.25055478816489,\n" +
            "      \"speed\" : 0.11082099278613973\n" +
            "    }, {\n" +
            "      \"point\" : \"CHIRON\",\n" +
            "      \"lon\" : 0.0,\n" +
            "      \"speed\" : 0.0\n" +
            "    } ],\n" +
            "    \"cusps\" : [ 0.0, 112.0733701445017, 124.00737005394461, 139.1600639036742, 161.2946267738389, 198.13498011949983, 252.80928120851658, 292.0733701445017, 304.0073700539446, 319.1600639036742, 341.2946267738389, 18.134980119499826, 72.80928120851658 ]\n" +
            "  }] }"

    @Test
    fun `Reading Json for charts should result in a correct instance of AllCharts`() {
        val mapper = ChartMapper()
        val jsonObject = JSONValue.parse(json) as JSONObject
        val result = mapper.jsonToAllCharts(jsonObject)
        result.name shouldBe "data_long_calculatedCharts.json"
        result.charts.size shouldBe 2
        result.charts[0].jdUt shouldBe (2407866.5507639353 plusOrMinus delta)
        result.charts[1].pointPositions[0].lon shouldBe (119.54973393244804 plusOrMinus delta)
    }
}