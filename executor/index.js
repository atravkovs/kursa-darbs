const axios = require("axios");

const suites = [
  {
    camundaVersion: 8,
    scenarios: ["one", "two", "three"],
    versions: ["main"],
    async start(scenario, version, attempt, variables) {
      const processDefinition = `scenario-${scenario}-${version}`;
      console.log(
        `Starting Camunda 8 "${processDefinition}" Process | Attempt: ${attempt}`
      );
      try {
        await axios.post(`http://localhost:8080/start/${processDefinition}`, {
          variables: { ...variables, scenario: processDefinition },
        });
      } catch (err) {
        console.error("Camunda 8 Start", err.message);
      }
    },
  },
  {
    camundaVersion: 7,
    scenarios: ["one", "two", "three"],
    versions: ["main", "async", "rest", "rest-async"],
    async start(scenario, version, attempt, variables) {
      const processDefinition = `scenario-${scenario}-${version}`;

      console.log(
        `Starting Camunda 7 "${processDefinition}" Process | Attempt: ${attempt}`
      );

      try {
        await axios.post(
          `http://localhost:8090/engine-rest/process-definition/key/${processDefinition}/start`,
          {
            variables: {
              scenario: { value: processDefinition, type: "String" },
              imageSize: { value: variables.imageSize, type: "Long" },
              maxIterations: { value: variables.maxIterations, type: "Long" },
            },
            businessKey: `Scenario ${processDefinition} attempt ${attempt}`,
          }
        );
      } catch (err) {
        console.error("Camunda 7 Start", err.message);
      }
    },
  },
];

async function timeout(time) {
  return new Promise((resolve) => setTimeout(() => resolve(), time));
}

async function executeSuite(suiteConfiguration) {
  const {
    camundaVersion,
    imageSize,
    maxIterations,
    throttle,
    scenarioDelay,
    tries,
    scenario,
    variant,
  } = suiteConfiguration;

  const suite = suites.find((s) => s.camundaVersion === camundaVersion);

  if (!suite) {
    console.error(`Suite for ${camundaVersion} camunda version not found`);
    return;
  }

  for (const suiteScenario of suite.scenarios) {
    if (scenario !== "all" && scenario !== suiteScenario) {
      continue;
    }

    for (const version of suite.versions) {
      if (variant !== "all" && variant !== version) {
        continue;
      }

      for (let i = 0; i < tries; i++) {
        suite.start(suiteScenario, version, i + 1, {
          imageSize,
          maxIterations,
        });
        await timeout(throttle);
      }

      console.log(`Waiting ${scenarioDelay} seconds before version runs`);
      await timeout(scenarioDelay * 1000);
    }
  }
}

(async () => {
  await require("yargs")
    .scriptName("camunda-test-executor")
    .usage("$0 <cmd> [args]")
    .command(
      "hello [name]",
      "Display a welcome message",
      (yargs) => {
        yargs.positional("name", {
          type: "string",
          default: "World",
          describe: "the name to say hello to",
        });
      },
      async (argv) => {
        console.log("hello", argv.name, "!");
      }
    )
    .command(
      "suite <camundaVersion> <imageSize> <maxIterations>",
      "Runs all test suites",
      (yargs) => {
        yargs.positional("camundaVersion", {
          type: "number",
          choices: [7, 8],
          describe: "Version of camunda (7 or 8)",
        });
        yargs.positional("imageSize", {
          type: "number",
          describe: "Size of mandelbrot image",
        });
        yargs.positional("maxIterations", {
          type: "number",
          describe: "Max iterations of mandelbrot set",
        });
        yargs.option("scenario", {
          type: "string",
          choices: ["all", "one", "two", "three"],
          describe: "scenario to execute",
          default: "all",
        });
        yargs.option("variant", {
          type: "string",
          choices: ["all", "main", "async", "rest", "rest-async"],
          describe: "scenario version to execute",
          default: "all",
        });
        yargs.option("throttle", {
          type: "number",
          describe: "delay between attempt execution in milliseconds",
          default: 1000,
        });
        yargs.option("scenarioDelay", {
          type: "number",
          describe: "delay between scenario execution in seconds",
          default: 30,
        });
        yargs.option("tries", {
          type: "number",
          describe: "amount of times each test case will be executed",
          default: 10,
        });
      },
      async (argv) => {
        const {
          camundaVersion,
          imageSize,
          maxIterations,
          throttle,
          scenarioDelay,
          tries,
          scenario,
          variant,
        } = argv;

        console.log(`[START] at ${new Date().toISOString()}`);

        await executeSuite({
          camundaVersion,
          imageSize,
          maxIterations,
          throttle,
          scenarioDelay,
          tries,
          scenario,
          variant,
        });

        console.log(`[END] at ${new Date().toISOString()}`);
      }
    )
    .help()
    .parse();
})();
