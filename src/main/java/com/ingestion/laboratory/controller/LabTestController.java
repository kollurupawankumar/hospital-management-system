package com.ingestion.laboratory.controller;

import com.ingestion.laboratory.model.LabTest;
import com.ingestion.laboratory.model.TestParameter;
import com.ingestion.laboratory.service.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/laboratory/tests")
public class LabTestController {

    @Autowired
    private LabTestService labTestService;

    @GetMapping
    public String getAllTests(Model model) {
        List<LabTest> tests = labTestService.findAll();
        model.addAttribute("tests", tests);
        return "laboratory/tests/list";
    }

    @GetMapping("/active")
    public String getActiveTests(Model model) {
        List<LabTest> tests = labTestService.findAllActiveTests();
        model.addAttribute("tests", tests);
        return "laboratory/tests/list";
    }

    @GetMapping("/category/{category}")
    public String getTestsByCategory(@PathVariable String category, Model model) {
        List<LabTest> tests = labTestService.findByCategory(category);
        model.addAttribute("tests", tests);
        model.addAttribute("category", category);
        return "laboratory/tests/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("labTest", new LabTest());
        model.addAttribute("parameters", new ArrayList<TestParameter>());
        model.addAttribute("categories", labTestService.findAllActiveCategories());
        model.addAttribute("departments", labTestService.findAllActiveDepartments());
        model.addAttribute("sampleTypes", labTestService.findAllActiveSampleTypes());
        return "laboratory/tests/create";
    }

    @PostMapping
    public String createTest(@Valid @ModelAttribute("labTest") LabTest labTest,
                            BindingResult result,
                            @RequestParam(value = "parameters", required = false) List<TestParameter> parameters,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("parameters", parameters != null ? parameters : new ArrayList<>());
            model.addAttribute("categories", labTestService.findAllActiveCategories());
            model.addAttribute("departments", labTestService.findAllActiveDepartments());
            model.addAttribute("sampleTypes", labTestService.findAllActiveSampleTypes());
            return "laboratory/tests/create";
        }

        try {
            LabTest savedTest = labTestService.createTest(labTest, parameters);
            redirectAttributes.addFlashAttribute("successMessage", "Test created successfully");
            return "redirect:/laboratory/tests/" + savedTest.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating test: " + e.getMessage());
            model.addAttribute("parameters", parameters != null ? parameters : new ArrayList<>());
            model.addAttribute("categories", labTestService.findAllActiveCategories());
            model.addAttribute("departments", labTestService.findAllActiveDepartments());
            model.addAttribute("sampleTypes", labTestService.findAllActiveSampleTypes());
            return "laboratory/tests/create";
        }
    }

    @GetMapping("/{id}")
    public String getTest(@PathVariable Long id, Model model) {
        Optional<LabTest> testOpt = labTestService.findById(id);
        if (testOpt.isPresent()) {
            model.addAttribute("labTest", testOpt.get());
            return "laboratory/tests/view";
        } else {
            model.addAttribute("errorMessage", "Test not found");
            return "redirect:/laboratory/tests";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<LabTest> testOpt = labTestService.findById(id);
        if (testOpt.isPresent()) {
            model.addAttribute("labTest", testOpt.get());
            model.addAttribute("categories", labTestService.findAllActiveCategories());
            model.addAttribute("departments", labTestService.findAllActiveDepartments());
            model.addAttribute("sampleTypes", labTestService.findAllActiveSampleTypes());
            return "laboratory/tests/edit";
        } else {
            model.addAttribute("errorMessage", "Test not found");
            return "redirect:/laboratory/tests";
        }
    }

    @PostMapping("/{id}")
    public String updateTest(@PathVariable Long id,
                            @Valid @ModelAttribute("labTest") LabTest labTest,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", labTestService.findAllActiveCategories());
            model.addAttribute("departments", labTestService.findAllActiveDepartments());
            model.addAttribute("sampleTypes", labTestService.findAllActiveSampleTypes());
            return "laboratory/tests/edit";
        }

        try {
            LabTest updatedTest = labTestService.updateTest(id, labTest);
            redirectAttributes.addFlashAttribute("successMessage", "Test updated successfully");
            return "redirect:/laboratory/tests/" + updatedTest.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating test: " + e.getMessage());
            model.addAttribute("categories", labTestService.findAllActiveCategories());
            model.addAttribute("departments", labTestService.findAllActiveDepartments());
            model.addAttribute("sampleTypes", labTestService.findAllActiveSampleTypes());
            return "laboratory/tests/edit";
        }
    }

    @GetMapping("/{id}/parameters/new")
    public String showAddParameterForm(@PathVariable Long id, Model model) {
        Optional<LabTest> testOpt = labTestService.findById(id);
        if (testOpt.isPresent()) {
            model.addAttribute("labTest", testOpt.get());
            model.addAttribute("parameter", new TestParameter());
            return "laboratory/tests/add-parameter";
        } else {
            model.addAttribute("errorMessage", "Test not found");
            return "redirect:/laboratory/tests";
        }
    }

    @PostMapping("/{id}/parameters")
    public String addParameter(@PathVariable Long id,
                              @Valid @ModelAttribute("parameter") TestParameter parameter,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            Optional<LabTest> testOpt = labTestService.findById(id);
            if (testOpt.isPresent()) {
                model.addAttribute("labTest", testOpt.get());
            }
            return "laboratory/tests/add-parameter";
        }

        try {
            labTestService.addParameter(id, parameter);
            redirectAttributes.addFlashAttribute("successMessage", "Parameter added successfully");
            return "redirect:/laboratory/tests/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding parameter: " + e.getMessage());
            Optional<LabTest> testOpt = labTestService.findById(id);
            if (testOpt.isPresent()) {
                model.addAttribute("labTest", testOpt.get());
            }
            return "laboratory/tests/add-parameter";
        }
    }

    @GetMapping("/{testId}/parameters/{parameterId}/edit")
    public String showEditParameterForm(@PathVariable Long testId,
                                       @PathVariable Long parameterId,
                                       Model model) {
        Optional<LabTest> testOpt = labTestService.findById(testId);
        if (testOpt.isPresent()) {
            LabTest test = testOpt.get();
            Optional<TestParameter> paramOpt = test.getParameters().stream()
                    .filter(p -> p.getId().equals(parameterId))
                    .findFirst();
            
            if (paramOpt.isPresent()) {
                model.addAttribute("labTest", test);
                model.addAttribute("parameter", paramOpt.get());
                return "laboratory/tests/edit-parameter";
            }
        }
        
        model.addAttribute("errorMessage", "Test or parameter not found");
        return "redirect:/laboratory/tests/" + testId;
    }

    @PostMapping("/{testId}/parameters/{parameterId}")
    public String updateParameter(@PathVariable Long testId,
                                 @PathVariable Long parameterId,
                                 @Valid @ModelAttribute("parameter") TestParameter parameter,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            Optional<LabTest> testOpt = labTestService.findById(testId);
            if (testOpt.isPresent()) {
                model.addAttribute("labTest", testOpt.get());
            }
            return "laboratory/tests/edit-parameter";
        }

        try {
            labTestService.updateParameter(testId, parameterId, parameter);
            redirectAttributes.addFlashAttribute("successMessage", "Parameter updated successfully");
            return "redirect:/laboratory/tests/" + testId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating parameter: " + e.getMessage());
            Optional<LabTest> testOpt = labTestService.findById(testId);
            if (testOpt.isPresent()) {
                model.addAttribute("labTest", testOpt.get());
            }
            return "laboratory/tests/edit-parameter";
        }
    }

    @PostMapping("/{testId}/parameters/{parameterId}/delete")
    public String deleteParameter(@PathVariable Long testId,
                                 @PathVariable Long parameterId,
                                 RedirectAttributes redirectAttributes) {
        try {
            labTestService.removeParameter(testId, parameterId);
            redirectAttributes.addFlashAttribute("successMessage", "Parameter deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting parameter: " + e.getMessage());
        }
        return "redirect:/laboratory/tests/" + testId;
    }

    @PostMapping("/{id}/activate")
    public String activateTest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labTestService.activateTest(id);
            redirectAttributes.addFlashAttribute("successMessage", "Test activated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error activating test: " + e.getMessage());
        }
        return "redirect:/laboratory/tests/" + id;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivateTest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labTestService.deactivateTest(id);
            redirectAttributes.addFlashAttribute("successMessage", "Test deactivated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating test: " + e.getMessage());
        }
        return "redirect:/laboratory/tests/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteTest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labTestService.deleteTest(id);
            redirectAttributes.addFlashAttribute("successMessage", "Test deleted successfully");
            return "redirect:/laboratory/tests";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting test: " + e.getMessage());
            return "redirect:/laboratory/tests/" + id;
        }
    }
}