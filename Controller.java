@GetMapping("/myapp-form")
public String myAppForm() {
    return "myapp-form";
}

@GetMapping("/mydeploy-form")
public String myDeployForm() {
    return "mydeploy-form";
}

@PostMapping("/generate-myapp")
public void generateMyApp(
        @RequestParam String groupId,
        @RequestParam String artifactId,
        @RequestParam String packageName,
        @RequestParam String version,
        HttpServletResponse response) throws IOException {

    byte[] zip = service.generateMyAppZip(groupId, artifactId, packageName, version);

    response.setContentType("application/zip");
    response.setHeader("Content-Disposition", "attachment; filename=myApp.zip");
    response.getOutputStream().write(zip);
}

@PostMapping("/generate-mydeploy")
public void generateMyDeploy(
        @RequestParam String groupId,
        @RequestParam String artifactId,
        @RequestParam String version,
        HttpServletResponse response) throws IOException {

    byte[] zip = service.generateMyDeployZip(groupId, artifactId, version);

    response.setContentType("application/zip");
    response.setHeader("Content-Disposition", "attachment; filename=myDeploy.zip");
    response.getOutputStream().write(zip);
}
