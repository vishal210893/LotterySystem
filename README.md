# Lottery Ticket REST API

Welcome to the Lottery Ticket REST API! This project provides a RESTful interface for managing and interacting with a simple lottery system. The API allows you to create tickets, check their status, and amend them as needed, all while following a specific set of lottery rules.

## Table of Contents

- [Lottery Rules](#lottery-rules)
- [Implementation Details](#implementation-details)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Lottery Rules

A lottery ticket consists of several lines, each with 3 numbers. The values of these numbers can be 0, 1, or 2. The result for each line is determined as follows:

1. If the sum of the values on a line is 2, the result for that line is 10.
2. If all three numbers on a line are the same, the result is 5.
3. If the 2nd and 3rd numbers are different from the 1st number, the result is 1.
4. In all other cases, the result is 0.

## Implementation Details

The API includes the following endpoints:

- **Create a Ticket:** `POST /ticket` - Create a new lottery ticket with a specified number of lines.
- **Get List of Tickets:** `GET /ticket` - Retrieve a list of all existing tickets.
- **Get Individual Ticket:** `GET /ticket/{id}` - Retrieve details of a specific ticket by its ID.
- **Amend Ticket:** `PUT /ticket/{id}` - Update the lines of an existing ticket (amendments are allowed only if the ticket status has not been checked).
- **Retrieve Ticket Status:** `PUT /status/{id}` - Check the status of a ticket, which includes evaluating each line based on the lottery rules and returning the results.

The code is designed to be clean, modular, and adheres to RESTful principles. It includes comprehensive tests to ensure functionality and reliability.

## Contributing
We welcome contributions to enhance and improve the API. To contribute:

- Fork the repository.
- Create a feature branch (git checkout -b feature/your-feature).
- Commit your changes (git commit -am 'Add new feature').
- Push to the branch (git push origin feature/your-feature).
- Create a new Pull Request.
  
Please ensure your contributions adhere to the project's coding standards and include tests where applicable.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
