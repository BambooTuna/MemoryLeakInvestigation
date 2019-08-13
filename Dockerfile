FROM hseeberger/scala-sbt:8u181_2.12.8_1.2.8

ARG project_dir=/application
WORKDIR $project_dir
COPY ./ $project_dir

CMD [ "sbt", "boot/run" ]