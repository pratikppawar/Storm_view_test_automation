#from beaver.machine import Machine
import pytest
import logging
import shutil
from beaver.maven import Maven
from beaver import util
from beaver.component.storm import Storm
from beaver.config import Config
from beaver.component.tez_ui import TezUI
import os

logger = logging.getLogger(__name__)
SRC_DIR = os.path.join(Config.getEnv('WORKSPACE'), 'tests', 'storm', 'ui')
#LOCAL_WORK_DIR = os.path.join(Config.getEnv('ARTIFACTS_DIR'), 'storm', 'ui')

LOCAL_WORK_DIR = os.path.join(Config.getEnv('ARTIFACTS_DIR'))
TESTCASES = []
TEST_RESULT = {}

def collect_test_results():
    test_result = {}
    suiteResults = util.findMatchingFiles(os.path.join(LOCAL_WORK_DIR, 'target','surefire-reports','TBA'), 'TBA')
    for suiteResult in suiteResults:
        test_result.update(util.parseJUnitXMLResult(suiteResult))
    for key, value in test_result.items():
        TEST_RESULT[key] = value
    TESTCASES.extend(sorted(TEST_RESULT.keys()))

def deployTopologies():
    testProperties = {}
    logger.info("Start Deploying topologies")
    shortClassName='WordCountTopology'

    (returnCode1, stdOut) = Storm.runStormStarterTopology( shortClassName, arg='Wordcount', env=None, logoutput=True)

    shortClassName='ExclamationTopology'
    (returnCode2, stdOut) = Storm.runStormStarterTopology(shortClassName, arg='Excl', env=None, logoutput=True)

    shortClassName='ReachTopology'
    (returnCode3, stdOut) = Storm.runStormStarterTopology(shortClassName, arg='Reach', env=None, logoutput=True)

    if (returnCode1 & returnCode2 & returnCode3):
        logger.info("Topology deployment failed")
        testProperties['test.topologies.deployed'] = "false"
    testProperties['test.topologies.deployed'] = "true"
    util.writePropertiesToFile(os.path.join(SRC_DIR, 'resources', 'config.properties'),os.path.join(LOCAL_WORK_DIR, 'config.properties'), testProperties)
    logger.info("Finished Deploying topologies")

def generate_tests():

    deployTopologies()
    logger.info(SRC_DIR+"PPP"+LOCAL_WORK_DIR)
    # Update config.properties with values
    testProperties = {}
    BROWSER = "firefox"
    logger.info("Browser is set to " + BROWSER)
    testProperties['test.browser'] = BROWSER

    logger.info("Set Ambari URL and hostname")
    testProperties['test.browser.set.url'] = TezUI.getAmbariUrl()
    testProperties['test.hostname'] = str(Storm.getWebUIHost())+':'+str(Storm.getWebUIPort())
    logger.info("Set values  in config.properties")

    for entry in testProperties:
        logger.info(str(testProperties))
       # util.writeToFile(str(entry)+'='+testProperties[entry]+'\n',os.path.join(SRC_DIR, 'resources', 'config.properties'),True)

    #util.writeToFile("File",os.path.join(SRC_DIR, 'resources', 'config.properties'),True)
    #util.writePropertiesToFile(os.path.join(SRC_DIR, 'resources', 'config.properties'), os.path.join(LOCAL_WORK_DIR, 'config.properties'), testProperties)

    # Execute tests via maven
    maven_options = "-DfailIfNoTests=false"
    mvn_cmd = 'clean test'
    exit_code, stdout = Maven.run('%s --fail-at-end' % mvn_cmd, mavenOpts=maven_options, cwd=SRC_DIR, env={"DISPLAY": ":99"})

generate_tests()
collect_test_results()

@pytest.mark.parametrize("tcid", TESTCASES)
def test_policymgr(tcid):
    """
    Description:
    test: ${tcid}
    """
    logger.info("processing: " + tcid)
    assert TEST_RESULT[tcid]['result'] == "pass", TEST_RESULT[tcid]['failure']

