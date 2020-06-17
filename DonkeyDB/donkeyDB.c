#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>

typedef struct {
    char *buffer;
    size_t buffer_length;
    int input_length;
} InputBuffer;

void print_prompt(void);
void print_error(const char *);
void close_input_buffer(InputBuffer *);
void read_input(InputBuffer *);
int getline(char **lineptr, size_t *n, FILE *stream);
InputBuffer *new_input_buffer();

int
main()
{
    InputBuffer* input_buffer = new_input_buffer();
    if (input_buffer == NULL)
        perror("Fail to create input buffer");
    while (true) {
        print_prompt();

        read_input(input_buffer);

        printf("debug(input line): %s\n", input_buffer->buffer);

        if (!strcmp(input_buffer->buffer,
                   ".exit")) {
            close_input_buffer(input_buffer);
            break;
        } else {
            print_error("unknown command");
        }

    }
    return 0;
}

/* getline: read a line into *lineptr, return length, without trailing '\n' */
int
getline(char **lineptr, size_t *n, FILE *stream)
{
    char line[521];
    int ch, i;
    for (i = 0; ((ch = fgetc(stream)) != EOF && ch != '\n'); ++i) {
        line[i] = ch;
        // printf("debug: %d %c\n", i, ch);
    }
    line[i] = '\0';
    *lineptr = (char *)(malloc((i + 2) * sizeof(char)));
    strcpy(*lineptr, line);
    *n = i + 1;
    return *n;
}

void
read_input(InputBuffer *input_buffer)
{
    free(input_buffer->buffer);

    int bytes_read = getline(&input_buffer->buffer,
                             &input_buffer->buffer_length,
                             stdin);
    
    if (bytes_read <= 0) {
        perror("Error reading input");
    }
}

InputBuffer 
*new_input_buffer()
{
    InputBuffer *input_buffer = (InputBuffer *)(malloc(sizeof(InputBuffer)));
    input_buffer->buffer = NULL;
    input_buffer->buffer_length = 0;
    input_buffer->input_length = 0;
    return input_buffer;
}

void
close_input_buffer(InputBuffer *input_buffer)
{
    free(input_buffer->buffer);
    free(input_buffer);
}

void
print_prompt()
{
    printf("donkeyDB> ");
}

void
print_error(const char *s)
{
    printf("Error: %s.\n", s);
}
