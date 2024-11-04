INSERT INTO users (user_name, nickname, role, password, created_by, created_at, modified_at)
VALUES
    ('admin0', 'admin', 'ADMIN', 'Zz12341234!', 'anonymousUser', now(), now()),
    ('user1', 'aaa', 'USER', 'Aa12341234!', 'anonymousUser', now(), now()),
    ('user2', 'bbb', 'USER', 'Bb12341234!', 'anonymousUser', now(), now()),
    ('user3', 'ccc', 'USER', 'Cc12341234!', 'anonymousUser', now(), now()),
    ('user4', 'ddd', 'USER', 'Dd12341234!', 'anonymousUser', now(), now());

INSERT INTO article (title, content, created_by, created_at, modified_at)
VALUES
    ('Lorem Ipsum', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 'user1', '2023-01-15 10:20:30', '2023-01-15 12:22:45'),
    ('Aliquam Eget', 'Aliquam eget ante nec quam varius tincidunt.', 'user1', '2023-02-10 14:35:15', '2023-02-10 16:47:20'),
    ('Vestibulum Ante', 'Vestibulum ante ipsum primis in faucibus orci luctus.', 'user2', '2023-03-05 09:15:50', '2023-03-05 11:45:00'),
    ('Mauris Vel', 'Mauris vel erat vitae lorem volutpat tincidunt.', 'user2', '2023-04-11 08:30:20', '2023-04-11 10:31:55'),
    ('Cras Convallis', 'Cras convallis justo at turpis consectetur, ut posuere libero.', 'user3', '2023-05-17 15:42:00', '2023-05-17 18:12:10'),
    ('Pellentesque Habit', 'Pellentesque habitant morbi tristique senectus et netus.', 'user3', '2023-06-20 11:50:40', '2023-06-20 13:21:35'),
    ('Ut Eleifend', 'Ut eleifend libero sit amet diam luctus, at feugiat metus.', 'user4', '2023-07-22 17:00:25', '2023-07-22 19:40:30'),
    ('Nunc Tempor', 'Nunc tempor, risus et vulputate fringilla, risus nulla.', 'user4', '2023-08-25 10:20:10', '2023-08-25 12:15:20');

INSERT INTO comment (content, article_id, parent_comment_id, created_by, created_at, modified_at)
VALUES
    ('Great article! Very informative.', 1, NULL, 'user2', '2023-01-16 10:25:00', '2023-01-16 10:25:00'),
    ('Thank you! Glad you liked it.', 1, 1, 'user1', '2023-01-16 11:00:00', '2023-01-16 11:00:00'),
    ('I have a question about the third point.', 1, 1, 'user3', '2023-01-16 12:15:00', '2023-01-16 12:15:00'),
    ('Could you provide more resources?', 1, 1, 'user4', '2023-01-16 13:45:00', '2023-01-16 13:45:00'),
    ('I found this really helpful, thanks!', 2, NULL, 'user3', '2023-02-11 15:10:30', '2023-02-11 15:10:30'),
    ('You’re welcome! Happy to help.', 2, 3, 'user1', '2023-02-11 16:00:00', '2023-02-11 16:00:00'),
    ('Can you elaborate on the second point?', 2, NULL, 'user4', '2023-02-12 09:20:15', '2023-02-12 09:20:15'),
    ('Sure! I’ll explain further in another post.', 2, 5, 'user1', '2023-02-12 10:30:45', '2023-02-12 10:30:45'),
    ('I didn’t quite understand the third example.', 2, 5, 'user2', '2023-02-12 11:15:00', '2023-02-12 11:15:00'),
    ('Good points, but I disagree with some parts.', 3, NULL, 'user4', '2023-03-06 12:15:00', '2023-03-06 12:15:00'),
    ('Which parts do you disagree with?', 3, 7, 'user2', '2023-03-06 13:00:00', '2023-03-06 13:00:00'),
    ('I feel the third paragraph could be more precise.', 3, 7, 'user3', '2023-03-06 13:45:00', '2023-03-06 13:45:00'),
    ('Interesting read, but could use more examples.', 4, NULL, 'user3', '2023-04-12 09:10:20', '2023-04-12 09:10:20'),
    ('Thanks for the feedback. I’ll add more in the future.', 4, 8, 'user2', '2023-04-12 10:45:50', '2023-04-12 10:45:50'),
    ('Could you give a specific example on topic X?', 4, 8, 'user1', '2023-04-12 11:30:00', '2023-04-12 11:30:00'),
    ('This is exactly what I was looking for!', 5, NULL, 'user4', '2023-05-18 16:15:40', '2023-05-18 16:15:40'),
    ('Glad it helped you!', 5, 10, 'user3', '2023-05-18 17:00:00', '2023-05-18 17:00:00'),
    ('Could you clarify the third paragraph?', 6, NULL, 'user2', '2023-06-21 12:35:25', '2023-06-21 12:35:25'),
    ('Absolutely! The third paragraph refers to...', 6, 11, 'user3', '2023-06-21 13:45:15', '2023-06-21 13:45:15'),
    ('I found that section confusing as well.', 6, 11, 'user4', '2023-06-21 14:10:45', '2023-06-21 14:10:45'),
    ('Very insightful, thanks for sharing.', 7, NULL, 'user1', '2023-07-23 18:20:45', '2023-07-23 18:20:45'),
    ('This was a great read!', 8, NULL, 'user3', '2023-08-26 11:10:50', '2023-08-26 11:10:50'),
    ('Glad you enjoyed it!', 8, 15, 'user4', '2023-08-26 12:50:30', '2023-08-26 12:50:30'),
    ('I agree! It was really informative.', 8, 15, 'user2', '2023-08-26 13:15:00', '2023-08-26 13:15:00');
