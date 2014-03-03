for x in `ls *.jar`
do
  echo        '<dependency>'
  echo        '    <groupId>'$x'</groupId>'
  echo        '    <artifactId>jar</artifactId>'
  echo        '    <scope>system</scope>'
  echo        '    <version>${project.version}</version>'
  echo        '    <systemPath>${project.basedir}/libs/'$x'</systemPath>'
  echo        '</dependency>'
done
