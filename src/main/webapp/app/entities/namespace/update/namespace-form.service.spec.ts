import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../namespace.test-samples';

import { NamespaceFormService } from './namespace-form.service';

describe('Namespace Form Service', () => {
  let service: NamespaceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NamespaceFormService);
  });

  describe('Service methods', () => {
    describe('createNamespaceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNamespaceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing INamespace should create a new form with FormGroup', () => {
        const formGroup = service.createNamespaceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getNamespace', () => {
      it('should return NewNamespace for default Namespace initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createNamespaceFormGroup(sampleWithNewData);

        const namespace = service.getNamespace(formGroup) as any;

        expect(namespace).toMatchObject(sampleWithNewData);
      });

      it('should return NewNamespace for empty Namespace initial value', () => {
        const formGroup = service.createNamespaceFormGroup();

        const namespace = service.getNamespace(formGroup) as any;

        expect(namespace).toMatchObject({});
      });

      it('should return INamespace', () => {
        const formGroup = service.createNamespaceFormGroup(sampleWithRequiredData);

        const namespace = service.getNamespace(formGroup) as any;

        expect(namespace).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INamespace should not enable id FormControl', () => {
        const formGroup = service.createNamespaceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNamespace should disable id FormControl', () => {
        const formGroup = service.createNamespaceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
