import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../scope.test-samples';

import { ScopeFormService } from './scope-form.service';

describe('Scope Form Service', () => {
  let service: ScopeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScopeFormService);
  });

  describe('Service methods', () => {
    describe('createScopeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScopeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            meterDescription: expect.any(Object),
            meterName: expect.any(Object),
            meterUtility: expect.any(Object),
            pricePerMonth: expect.any(Object),
            contract: expect.any(Object),
            service: expect.any(Object),
          })
        );
      });

      it('passing IScope should create a new form with FormGroup', () => {
        const formGroup = service.createScopeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            meterDescription: expect.any(Object),
            meterName: expect.any(Object),
            meterUtility: expect.any(Object),
            pricePerMonth: expect.any(Object),
            contract: expect.any(Object),
            service: expect.any(Object),
          })
        );
      });
    });

    describe('getScope', () => {
      it('should return NewScope for default Scope initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createScopeFormGroup(sampleWithNewData);

        const scope = service.getScope(formGroup) as any;

        expect(scope).toMatchObject(sampleWithNewData);
      });

      it('should return NewScope for empty Scope initial value', () => {
        const formGroup = service.createScopeFormGroup();

        const scope = service.getScope(formGroup) as any;

        expect(scope).toMatchObject({});
      });

      it('should return IScope', () => {
        const formGroup = service.createScopeFormGroup(sampleWithRequiredData);

        const scope = service.getScope(formGroup) as any;

        expect(scope).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScope should not enable id FormControl', () => {
        const formGroup = service.createScopeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScope should disable id FormControl', () => {
        const formGroup = service.createScopeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
