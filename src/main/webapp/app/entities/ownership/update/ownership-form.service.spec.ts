import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ownership.test-samples';

import { OwnershipFormService } from './ownership-form.service';

describe('Ownership Form Service', () => {
  let service: OwnershipFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OwnershipFormService);
  });

  describe('Service methods', () => {
    describe('createOwnershipFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOwnershipFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            clientRef: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            meters: expect.any(Object),
            classifications: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });

      it('passing IOwnership should create a new form with FormGroup', () => {
        const formGroup = service.createOwnershipFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            clientRef: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            meters: expect.any(Object),
            classifications: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });
    });

    describe('getOwnership', () => {
      it('should return NewOwnership for default Ownership initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOwnershipFormGroup(sampleWithNewData);

        const ownership = service.getOwnership(formGroup) as any;

        expect(ownership).toMatchObject(sampleWithNewData);
      });

      it('should return NewOwnership for empty Ownership initial value', () => {
        const formGroup = service.createOwnershipFormGroup();

        const ownership = service.getOwnership(formGroup) as any;

        expect(ownership).toMatchObject({});
      });

      it('should return IOwnership', () => {
        const formGroup = service.createOwnershipFormGroup(sampleWithRequiredData);

        const ownership = service.getOwnership(formGroup) as any;

        expect(ownership).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOwnership should not enable id FormControl', () => {
        const formGroup = service.createOwnershipFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOwnership should disable id FormControl', () => {
        const formGroup = service.createOwnershipFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
