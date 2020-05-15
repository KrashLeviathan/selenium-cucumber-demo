Feature: Searching the Zirous website displays relevant results

  Background:
    Given I use the Chrome browser
    And I navigate to the Zirous home page

  Scenario Outline: Searching within the menu bar
    When I click the magnifying glass in the header
    And I type "<Search Input>"
    Then the page title should contain the search input
    And the search results should include:
      | Result A | <Result A> |
      | Result B | <Result B> |
      | Result C | <Result C> |

    Examples:
      | Search Input     | Result A               | Result B                  | Result C                                                 |

      | Machine Learning | Machine Learning Spark | Machine Learning & AI     |                                                          |
      | cloud infra      | Cloud Has Your Back    | Shroud of the Cloud       | Controlling Cloud Infrastructure with Serverless Compute |
      | java development | Landing Your Dream Job | NiFi vs. Kafka… Or Is It? |                                                          |
