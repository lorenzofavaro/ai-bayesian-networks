package bayes.factory;

import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.util.RandVar;
import bayes.CustomBayesNet;
import bayes.CustomNode;

public class BNFactory {

    @SuppressWarnings("unused")
    public static BayesianNetwork construct2FairDiceNetwork() {
        CustomNode dice1 = new CustomNode(ExampleRV.DICE_1_RV, new double[] {
                1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
                1.0 / 6.0 });
        CustomNode dice2 = new CustomNode(ExampleRV.DICE_2_RV, new double[] {
                1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
                1.0 / 6.0 });

        return new CustomBayesNet(dice1, dice2);
    }

    @SuppressWarnings("unused")
    public static BayesianNetwork constructToothacheCavityCatchNetwork() {
        CustomNode cavity = new CustomNode(ExampleRV.CAVITY_RV, new double[] {
                0.2, 0.8 });
        @SuppressWarnings("unused")
        CustomNode toothache = new CustomNode(ExampleRV.TOOTHACHE_RV,
                new double[] {
                        // C=true, T=true
                        0.6,
                        // C=true, T=false
                        0.4,
                        // C=false, T=true
                        0.1,
                        // C=false, T=false
                        0.9

                }, cavity);
        @SuppressWarnings("unused")
        CustomNode catchN = new CustomNode(ExampleRV.CATCH_RV, new double[] {
                // C=true, Catch=true
                0.9,
                // C=true, Catch=false
                0.1,
                // C=false, Catch=true
                0.2,
                // C=false, Catch=false
                0.8 }, cavity);

        return new CustomBayesNet(cavity);
    }

    @SuppressWarnings("unused")
    public static BayesianNetwork constructToothacheCavityCatchWeatherNetwork() {
        CustomNode cavity = new CustomNode(ExampleRV.CAVITY_RV, new double[] {
                0.2, 0.8 });
        @SuppressWarnings("unused")
        CustomNode toothache = new CustomNode(ExampleRV.TOOTHACHE_RV,
                new double[] {
                        // C=true, T=true
                        0.6,
                        // C=true, T=false
                        0.4,
                        // C=false, T=true
                        0.1,
                        // C=false, T=false
                        0.9

                }, cavity);
        @SuppressWarnings("unused")
        CustomNode catchN = new CustomNode(ExampleRV.CATCH_RV, new double[] {
                // C=true, Catch=true
                0.9,
                // C=true, Catch=false
                0.1,
                // C=false, Catch=true
                0.2,
                // C=false, Catch=false
                0.8 }, cavity);
        CustomNode weather = new CustomNode(ExampleRV.WEATHER_RV,
                new double[] {
                        // sunny
                        0.6,
                        // rain
                        0.1,
                        // cloudy
                        0.29,
                        // snow
                        0.01 });

        return new CustomBayesNet(cavity, weather);
    }

    @SuppressWarnings("unused")
    public static BayesianNetwork constructMeningitisStiffNeckNetwork() {
        CustomNode meningitis = new CustomNode(ExampleRV.MENINGITIS_RV,
                new double[] { 1.0 / 50000.0, 1.0 - (1.0 / 50000.0) });
        @SuppressWarnings("unused")
        CustomNode stiffneck = new CustomNode(ExampleRV.STIFF_NECK_RV,
                new double[] {
                        // M=true, S=true
                        0.7,
                        // M=true, S=false
                        0.3,
                        // M=false, S=true
                        0.009986199723994478,
                        // M=false, S=false
                        0.9900138002760055

                }, meningitis);
        return new CustomBayesNet(meningitis);
    }

    @SuppressWarnings("unused")
    public static BayesianNetwork constructBurglaryAlarmNetwork() {
        CustomNode burglary = new CustomNode(ExampleRV.BURGLARY_RV,
                new double[] { 0.001, 0.999 });
        CustomNode earthquake = new CustomNode(ExampleRV.EARTHQUAKE_RV,
                new double[] { 0.002, 0.998 });
        CustomNode alarm = new CustomNode(ExampleRV.ALARM_RV, new double[] {
                // B=true, E=true, A=true
                0.95,
                // B=true, E=true, A=false
                0.05,
                // B=true, E=false, A=true
                0.94,
                // B=true, E=false, A=false
                0.06,
                // B=false, E=true, A=true
                0.29,
                // B=false, E=true, A=false
                0.71,
                // B=false, E=false, A=true
                0.001,
                // B=false, E=false, A=false
                0.999 }, burglary, earthquake);
        @SuppressWarnings("unused")
        CustomNode johnCalls = new CustomNode(ExampleRV.JOHN_CALLS_RV,
                new double[] {
                        // A=true, J=true
                        0.90,
                        // A=true, J=false
                        0.10,
                        // A=false, J=true
                        0.05,
                        // A=false, J=false
                        0.95 }, alarm);
        @SuppressWarnings("unused")
        CustomNode maryCalls = new CustomNode(ExampleRV.MARY_CALLS_RV,
                new double[] {
                        // A=true, M=true
                        0.70,
                        // A=true, M=false
                        0.30,
                        // A=false, M=true
                        0.01,
                        // A=false, M=false
                        0.99 }, alarm);

        return new CustomBayesNet(burglary, earthquake);
    }
    @SuppressWarnings("unused")
    public static BayesianNetwork constructCloudySprinklerRainWetGrassSlipperyRoadNetwork() {
        CustomNode cloudy = new CustomNode(ExampleRV.CLOUDY_RV, new double[] {
                0.5, 0.5 });
        CustomNode sprinkler = new CustomNode(ExampleRV.SPRINKLER_RV,
                new double[] {
                        // Cloudy=true, Sprinkler=true
                        0.1,
                        // Cloudy=true, Sprinkler=false
                        0.9,
                        // Cloudy=false, Sprinkler=true
                        0.5,
                        // Cloudy=false, Sprinkler=false
                        0.5 }, cloudy);
        CustomNode rain = new CustomNode(ExampleRV.RAIN_RV, new double[] {
                // Cloudy=true, Rain=true
                0.8,
                // Cloudy=true, Rain=false
                0.2,
                // Cloudy=false, Rain=true
                0.2,
                // Cloudy=false, Rain=false
                0.8 }, cloudy);
        CustomNode wetGrass = new CustomNode(ExampleRV.WET_GRASS_RV,
                new double[] {
                        // Sprinkler=true, Rain=true, WetGrass=true
                        .99,
                        // Sprinkler=true, Rain=true, WetGrass=false
                        .01,
                        // Sprinkler=true, Rain=false, WetGrass=true
                        .9,
                        // Sprinkler=true, Rain=false, WetGrass=false
                        .1,
                        // Sprinkler=false, Rain=true, WetGrass=true
                        .9,
                        // Sprinkler=false, Rain=true, WetGrass=false
                        .1,
                        // Sprinkler=false, Rain=false, WetGrass=true
                        0.0,
                        // Sprinkler=false, Rain=false, WetGrass=false
                        1.0 }, sprinkler, rain);
        CustomNode slipperyRoad = new CustomNode(new RandVar("SlipperyRoad",
                new BooleanDomain()),
                new double[] {
                        // Rain=true, SlipperyRoad=true
                        .7,
                        // Rain=true, SlipperyRoad=false
                        .3,
                        // Rain=false, SlipperyRoad=true
                        0.0,
                        // Rain=false, SlipperyRoad=false
                        1.0}, rain);

        return new CustomBayesNet(cloudy);
    }
}
