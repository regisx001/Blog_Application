# Blog Platform

The Blog Platform is designed to provide users with a space to share their thoughts, experiences, and expertise through written content. This platform offers an intuitive interface for creating, managing, and publishing blog posts, while engaging with readers through comments and social sharing features.

## Features

### Core Features

#### Content Creation

- Users can create new blog posts using a rich text editor.
- The editor supports formatting options (bold, italic, headings, lists).
- Each post must have a title and content body.
- Users can edit existing posts with all changes being saved.
- Users can delete posts with a confirmation prompt.
- Posts display an estimated reading time.

#### Content Organization

- Users can assign one category to each post.
- Users can add multiple tags to each post.
- Users can create and delete categories.
- Users can create and delete tags.
- The platform displays category and tag counts.

#### Draft Management

- Posts can be saved as drafts.
- Users can preview drafts before publishing.
- Draft posts show the last modified date.

### Bonus Features

#### Reader Engagement

- Readers can leave comments on published posts.
- Comments support basic formatting options.
- Comments show timestamps and author information.

#### Content Discovery

- Full-text search across all published posts.
- Search results can be filtered by category or tag.
- Search results can be sorted by date or relevance.
- The platform provides an RSS feed for subscribers.
- Related posts are suggested based on categories and tags.
- Posts include SEO metadata for better visibility.
- Archive view shows posts organized by date.

## Definitions

- **Post**: A blog post is a piece of written content that includes a title, body text, optional images, tags, and publication date. Posts can be drafts or published articles.
- **Category**: A broad classification used to organize blog posts into main themes or topics (e.g., "Technology", "Travel", "Personal Development").
- **Tag**: A specific keyword or phrase that describes the content of a post in more detail than categories. Tags are more granular and flexible, and a single post can have multiple tags.

## Tech Stack

- **Backend**: Java and Spring Boot
- **Build Tool**: Maven
- **Database**: PostgreSQL

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/regisx001/Blog_Application.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Blog_Application
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. The application will start on `http://localhost:8080`.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit your changes.
   ```bash
   git commit -m "Add your feature or fix description"
   ```
4. Push your branch to your forked repository.
   ```bash
   git push origin feature/your-feature-name
   ```
5. Open a pull request to the main repository.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- Inspired by popular blogging platforms like Medium, WordPress, and Blogger.

## Contact

For questions or support, please contact [regisx001](https://github.com/regisx001).
