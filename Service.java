public byte[] generateMyAppZip(String g, String a, String p, String v) throws IOException {

    Path temp = Files.createTempDirectory("gen");
    Path root = temp.resolve("myApp");
    Files.createDirectories(root);

    Map<String, Object> data = Map.of(
        "groupId", g,
        "artifactId", a,
        "packageName", p,
        "version", v
    );

    Path javaDir = root.resolve("src/main/java/" + p.replace('.', '/'));
    Path resDir  = root.resolve("src/main/resources");
    Files.createDirectories(javaDir);
    Files.createDirectories(resDir);

    generate("pom.xml.ftl", root.resolve("pom.xml"), data);
    generate("Application.ftl", javaDir.resolve(cap(a) + "Application.java"), data);
    generate("application.properties.ftl", resDir.resolve("application.properties"), data);

    return ZipUtil.zipDirectoryToBytes(root);
}
Generate only myDeploy
java
Copy code
public byte[] generateMyDeployZip(String g, String a, String v) throws IOException {

    Path temp = Files.createTempDirectory("gen");
    Path root = temp.resolve("myDeploy");
    Files.createDirectories(root);

    Map<String, Object> data = Map.of(
        "groupId", g,
        "artifactId", a,
        "version", v
    );

    generateDeploymentFolder(root, data);  // auto-copy everything from templates/releng

    return ZipUtil.zipDirectoryToBytes(root);
}
