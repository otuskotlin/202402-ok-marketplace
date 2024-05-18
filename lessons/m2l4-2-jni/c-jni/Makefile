JDK=/usr/lib/jvm/java-17-openjdk-amd64
INC=-I../build/jniIncludes -I/usr/include -I/usr/include/x86_64-linux-gnu -I$(JDK)/include -I$(JDK)/include/linux

#all: libc_jni.so

CC = gcc
OUT_FILE_NAME = libc_jni.so

CFLAGS= -fPIC -O0 -g -Wall -c -fpermissive

OBJ_DIR=../build/c/obj

OUT_DIR=../build/c/lib

# Enumerating of every *.cpp as *.o and using that as dependency.
# filter list of .c files in a directory.
# FILES =dump_l.c \
#	kter.c \
#
# $(OUT_FILE_NAME): $(patsubst %.c,$(OBJ_DIR)/%.o,$(wildcard $(FILES)))


# Enumerating of every *.cpp as *.o and using that as dependency
#$(OUT_FILE_NAME): $(patsubst %.c,$(OBJ_DIR)/%.o,$(wildcard *.c))
#	ar -r -o $(OUT_DIR)/$@ $^
$(OUT_FILE_NAME): $(patsubst %.c,$(OBJ_DIR)/%.o,$(wildcard *.c))
	$(CC) -shared -o $(OUT_DIR)/$@ $^



#Compiling every *.cpp to *.o
$(OBJ_DIR)/%.o: %.c dirmake
	$(CC) -c $(INC) $(CFLAGS) -o $@  $<

dirmake:
	@mkdir -p $(OUT_DIR)
	@mkdir -p $(OBJ_DIR)

clean:
	rm -f $(OBJ_DIR)/*.o $(OUT_DIR)/$(OUT_FILE_NAME) Makefile.bak

rebuild: clean build
