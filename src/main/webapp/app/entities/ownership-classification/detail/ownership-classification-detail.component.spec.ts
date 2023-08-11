import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnershipClassificationDetailComponent } from './ownership-classification-detail.component';

describe('OwnershipClassification Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnershipClassificationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OwnershipClassificationDetailComponent,
              resolve: { ownershipClassification: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OwnershipClassificationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ownershipClassification on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OwnershipClassificationDetailComponent);

      // THEN
      expect(instance.ownershipClassification).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
