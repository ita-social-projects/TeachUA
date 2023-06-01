UPDATE certificate_templates
SET course_description = dates.course_description,
    picture_path = dates.picture_path,
    project_description = dates.project_description
FROM (
    SELECT course_description, picture_path, project_description
    FROM certificate_dates
    WHERE id = 1) AS dates;