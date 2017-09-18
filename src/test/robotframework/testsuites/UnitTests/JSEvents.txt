*** Settings ***
Suite Setup       Open Page
Suite Teardown    Close All Browsers
Resource          ../../settings/Settings.txt

*** Variables ***
${URL Application}     http://fiddle.jshell.net/ShPVX/show/

*** Keywords ***
Open Page
    Open Browser    ${URL Application}    ${browser}
    Select Frame  xpath=//iframe
    Wait Until Page Contains Element    xpath=//select[@name='test']
    
*** Test Cases ***
Change dropdown value
    Select From List    xpath=//select[@name='test']    10
    Capture Page Screenshot
    Page Should Not Contain Element    xpath=//div[@id='2']
    Page Should Contain Element    xpath=//div[@id='10']