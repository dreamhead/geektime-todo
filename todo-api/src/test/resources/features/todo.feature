Feature: Todo Item

  Scenario: List todo item
    Given todo item "foo" is added
    And todo item "bar" is added
    When list todo item
    Then todo item "foo" should be contained
    And todo item "bar" should be contained

  Scenario: Mark todo item as done
    Given todo item "foo" is added
    And todo item "bar" is added
    When mark todo item "foo" as done
    And list todo item
    Then todo item "foo" should not be contained
    And todo item "bar" should be contained
